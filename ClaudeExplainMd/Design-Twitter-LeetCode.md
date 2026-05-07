# LeetCode 355 — Design Twitter

> **Problem:** Design a simplified Twitter where users can post tweets, follow/unfollow others,
> and retrieve the 10 most recent tweets in their news feed (own + followees).

---

## Core Data Model (shared by all solutions)

```
tweets:    userId → [Tweet(id, time), Tweet(id, time), ...]   // oldest → newest
following: userId → {followeeId, followeeId, ...}
timestamp: global Int, incremented on every postTweet()
```

**Why a global timestamp instead of wall-clock time?**
Tweets arrive sequentially in the problem. A monotonic counter is cheaper, collision-free,
and gives a total order — everything you need to rank tweets.

**Why store tweets oldest→newest (append-to-tail)?**
The heap solution walks backwards from the tail (newest first) without reversing the list.
`lastIndex` points straight at the newest tweet.

---

## Shared Kotlin Constructs

| Construct                           | Meaning                                                                                          |
|-------------------------------------|--------------------------------------------------------------------------------------------------|
| `mutableMapOf<K, V>()`              | Creates a `LinkedHashMap` under the hood                                                         |
| `getOrPut(key) { default }`         | Returns existing value or inserts+returns the default                                            |
| `mutableSetOf()`                    | `LinkedHashSet` — preserves insertion order, O(1) lookup                                         |
| `?: emptySet()`                     | Elvis: use right-hand side if left is null                                                       |
| `(setA) + element`                  | Returns a new immutable `Set` — does NOT mutate `setA`                                           |
| `data class`                        | Auto-generates `equals`, `hashCode`, `copy`, `toString`, destructuring                           |
| Destructuring `val (a, b, c) = obj` | Calls `component1()`, `component2()`, `component3()` — works on `data class` and `Pair`/`Triple` |

---

## Solution 1 — Naive Heap with `Triple` (original)

### Intuition

Each user's tweet list is already sorted by time (append order = time order).
This is a classic **K-way merge** problem: given K sorted lists, produce the top-N merged result.

The optimal structure for K-way merge is a **max-heap** — always pop the globally newest tweet,
then push that user's next-older tweet. You never sort more than you consume.

```
User A tweets: [t1, t3, t7]
User B tweets: [t2, t5, t9]
User C tweets: [t4, t6, t8]

Heap initially:  {t7(A), t9(B), t8(C)}
Poll t9(B) → push t5(B): {t7(A), t8(C), t5(B)}
Poll t8(C) → push t6(C): {t7(A), t6(C), t5(B)}
Poll t7(A) → push t3(A): ...
```

### The Problem with `Triple`

```kotlin
val maxHeap = PriorityQueue<Triple<Tweet, Int, Int>>(compareByDescending { it.first.time })
//                                   ↑      ↑   ↑
//                                 tweet  userId index
// "it.first" — what is .first? You must mentally map back to the Triple declaration.
```

`Triple.first`, `.second`, `.third` carry zero semantic meaning.
Every read requires consulting the declaration. This is the only real weakness of Solution 1.

### Line-by-Line

```kotlin
private fun getFeedSources(userId: Int): Set<Int> {
    return (followingMap[userId] ?: mutableSetOf()) + userId
    //      ↑ who they follow      ↑ empty if none    ↑ always include themselves
}
```

```kotlin
for (sourceId in getFeedSources(userId)) {
    val tweets = tweetMap[sourceId]
    if (!tweets.isNullOrEmpty()) {          // guard: user may have never tweeted
        val lastIdx = tweets.lastIndex      // index of newest tweet
        maxHeap.offer(Triple(tweets[lastIdx], sourceId, lastIdx))
        // seed heap with the newest tweet from each source
    }
}
```

```kotlin
while (maxHeap.isNotEmpty() && feed.size < 10) {
    val (tweet, sourceId, index) = maxHeap.poll()   // pop globally newest
    feed.add(tweet.id)
    if (index > 0) {                                // this source has older tweets
        val nextIdx = index - 1
        maxHeap.offer(Triple(tweetMap[sourceId]!![nextIdx], sourceId, nextIdx))
        // !! safe: we know this user has tweets (they're in the heap)
    }
}
```

---

## Solution 2 — Heap with `HeapEntry` data class (optimised)

### What changed and why

```kotlin
// Before:
Triple<Tweet, Int, Int>   →   it.first.time, it.second, it.third

// After:
data class HeapEntry(val tweet: Tweet, val userId: Int, val index: Int)
//                                          ↑ named fields — self-documenting
```

Destructuring now reads like prose:

```kotlin
val (tweet, sourceId, idx) = maxHeap.poll()   // crystal clear
```

### Other improvements

```kotlin
// ?: continue — skip sources with no tweets, cleaner than isNullOrEmpty check
val userTweets = tweets[sourceId] ?: continue

// buildList — expression-body style, avoids manual mutableListOf + return
return buildList {
    while (maxHeap.isNotEmpty() && size < 10) {
        val (tweet, sourceId, idx) = maxHeap.poll()
        add(tweet.id)
        if (idx > 0) maxHeap.offer(HeapEntry(tweets[sourceId]!![idx - 1], sourceId, idx - 1))
    }
}
```

`buildList { }` is a Kotlin stdlib builder that creates a `MutableList` internally,
returns it as an immutable `List`. Replaces the pattern:

```kotlin
val list = mutableListOf<T>()
// ... fill it ...
return list
```

```kotlin
// emptySet<Int>() instead of mutableSetOf()
// mutableSetOf() allocates a LinkedHashSet you immediately throw away after the + operation
// emptySet() returns a singleton — no allocation
private fun feedSources(userId: Int) = (following[userId] ?: emptySet<Int>()) + userId
```

### `class Twitter` vs `class Twitter()`

Empty primary constructor parens are redundant in Kotlin when there are no parameters.
Both compile identically — omitting `()` is idiomatic.

---

## Solution 3 — Flat Sort (LeetCode-constraint optimal)

### Intuition

Forget the heap. The problem says at most 500 users, 10,000 tweets total.
Just collect every relevant tweet, sort, slice.

```kotlin
fun getNewsFeed(userId: Int): List<Int> =
    feedSources(userId)
        .flatMap { tweets[it] ?: emptyList() }   // collect all tweets from all sources
        .sortedByDescending { it.time }           // sort newest-first
        .take(10)                                 // slice top 10
        .map { it.id }                            // extract IDs
```

This is a **single expression function** — the entire body is one pipeline, assigned with `=`.
Each operator is a standard Kotlin collection transform:

| Step                     | What it does                                                             |
|--------------------------|--------------------------------------------------------------------------|
| `flatMap { }`            | Maps each sourceId to their tweet list, then flattens all lists into one |
| `sortedByDescending { }` | Returns a new sorted list, original untouched                            |
| `take(10)`               | Returns first N elements (or fewer if list is shorter)                   |
| `map { it.id }`          | Transforms `List<Tweet>` → `List<Int>`                                   |

**Why `?: emptyList()` instead of `?: continue`?**
`continue` is a statement — only valid in `for` loops. Inside `flatMap`'s lambda,
you return a value. An empty list contributes nothing to the flattened result. Same effect.

---

## Complexity Comparison

|                        | Solution 1 (Triple Heap) | Solution 2 (HeapEntry) | Solution 3 (Flat Sort) |
|------------------------|--------------------------|------------------------|------------------------|
| `postTweet`            | O(1)                     | O(1)                   | O(1)                   |
| `getNewsFeed`          | O(K log K + 10 log K)    | O(K log K + 10 log K)  | O(M log M)             |
| `follow/unfollow`      | O(1)                     | O(1)                   | O(1)                   |
| Readability            | ⚠️ Triple hurts          | ✅ Clean                | ✅ Clearest             |
| Real-world scalability | ✅ Good                   | ✅ Best                 | ❌ Breaks at scale      |

> K = number of followees, M = total tweets across all followees.
> K log K ≪ M log M when users have many tweets — the heap shines there.
> Under LeetCode constraints M is tiny, making Solution 3 the pragmatic choice.

---

## When to use which

```
Interviewer asks "design for production scale" → Solution 2 (HeapEntry)
Interviewer asks "just make it work correctly" → Solution 3 (Flat Sort)
You want to show algorithm knowledge + clean code → Solution 2
You want to show idiomatic Kotlin fluency       → Solution 3
```

---

## Key Kotlin Takeaways

- Prefer **named data classes over `Triple`** the moment fields need meaning.
- **`buildList { }`** is the idiomatic replacement for manual list accumulation.
- **Expression body functions** (`= pipeline`) signal "this is a pure transformation."
- **`?: emptySet()` / `?: emptyList()`** avoid unnecessary allocation vs `?: mutableSetOf()`.
- **`?: continue`** in a `for` loop is cleaner than `if (!x.isNullOrEmpty())`.
- **`flatMap`** = map + flatten in one pass. Reach for it whenever you map to collections.