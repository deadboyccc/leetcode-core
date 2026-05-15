# LC 15 · Three Sum — Two Approaches Explained

---

## The Core Idea (Both Solutions)

Given an array, find all **unique triplets** `[a, b, c]` where `a + b + c = 0`.

**Shared skeleton — sort first, then fix + scan:**

```
Sort the array
For every index i, fix sorted[i] as the pivot "a"
Run a two-pointer scan on the subarray to the right to find b + c = -a
```

Sorting is the key enabler: once the array is sorted, you can *steer* two pointers deterministically — move `low` right to increase the sum, move `high` left to decrease it.

---

## Concrete Example Array

```
Input:  [-4, -1, -1, 0, 1, 2]
Sorted: [-4, -1, -1, 0, 1, 2]
         i   low            high
```

Expected output: `[[-1, -1, 2], [-1, 0, 1]]`

---

## Approach 1 — HashSet Deduplication

### Intuition

Fix each element as the pivot. Run a standard two-pointer 2Sum scan on the remaining subarray. **Don't worry about duplicates during the scan** — just throw every valid triplet into a `HashSet`. The set's equality semantics discard duplicates automatically.

### Code

```kotlin
fun threeSumHashSet(nums: IntArray): List<List<Int>> {
    var left = 0
    val sorted = nums.sorted()
    val res = hashSetOf<List<Int>>()

    while (left <= sorted.lastIndex) {
        val curNum = sorted[left]          // fix the pivot

        var low = left + 1
        var high = sorted.lastIndex

        while (low < high) {
            val sum = sorted[low] + sorted[high] + curNum
            if (sum == 0) res.add(listOf(curNum, sorted[low], sorted[high]))
            if (sum > 0) high-- else low++
        }

        left++
    }

    return res.toList()
}
```

### Step-by-Step Walkthrough

```
sorted = [-4, -1, -1, 0, 1, 2]

── left=0, pivot=-4 ──────────────────────────────────────
  low=1(-1), high=5(2)  → sum = -4 + -1 + 2 = -3  < 0 → low++
  low=2(-1), high=5(2)  → sum = -4 + -1 + 2 = -3  < 0 → low++
  low=3(0),  high=5(2)  → sum = -4 +  0 + 2 = -2  < 0 → low++
  low=4(1),  high=5(2)  → sum = -4 +  1 + 2 = -1  < 0 → low++
  low=5 == high=5 → inner loop ends, no hit

── left=1, pivot=-1 ──────────────────────────────────────
  low=2(-1), high=5(2)  → sum = -1 + -1 + 2 = 0  ✓ → add [-1,-1,2] to set
  low=2(-1), high=5(2)  → sum > 0 after add? No: sum=0 → low++ triggered by else
                           Actually: sum=0 is not >0, so low++ (else branch)
  low=3(0),  high=5(2)  → sum = -1 + 0 + 2 = 1   > 0 → high--
  low=3(0),  high=4(1)  → sum = -1 + 0 + 1 = 0  ✓ → add [-1,0,1] to set
  low=3(0),  high=4(1)  → sum=0, else → low++
  low=4 == high=4 → inner loop ends

── left=2, pivot=-1 ──────────────────────────────────────
  (same pivot value as left=1, same scan runs again)
  low=3(0),  high=5(2)  → sum = -1 + 0 + 2 = 1   > 0 → high--
  low=3(0),  high=4(1)  → sum = -1 + 0 + 1 = 0  ✓ → add [-1,0,1] — DUPLICATE, set ignores it
  ...

HashSet final: {[-1,-1,2], [-1,0,1]}
```

### Why the HashSet Works for Dedup

`List<Int>` equality in Kotlin/JVM is **structural** — two lists are equal if they contain the same elements in the same order. Since triplets are always added in sorted order (pivot ≤ low ≤ high), `[-1, 0, 1]` always hashes identically regardless of which iteration produced it.

### Complexity

| | |
|---|---|
| **Time** | O(n²) — O(n log n) sort + O(n²) two-pointer scan |
| **Space** | O(k) — HashSet stores up to k unique triplets |

**The hidden cost:** HashSet hashing and equality checks on `List<Int>` objects add constant-factor overhead per insertion. For k duplicate triplets you pay k hash computations even though only unique ones survive.

---

## Approach 2 — Explicit Duplicate Skipping (Optimal)

### Intuition

Same pivot + two-pointer skeleton, but instead of letting a HashSet absorb duplicates **after the fact**, you *skip duplicate values proactively* at each pointer:

- **Outer pointer `i`**: if `sorted[i] == sorted[i-1]`, this pivot was already fully explored → `continue`
- **Inner pointers after a hit**: advance past all consecutive equal values before moving on

No auxiliary structure needed — deduplication is baked into the pointer movement.

### Code

```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    val sorted = nums.sorted()
    val result = mutableListOf<List<Int>>()

    for (i in 0..sorted.lastIndex - 2) {
        // Duplicate pivot → entire scan would be identical → skip
        if (i > 0 && sorted[i] == sorted[i - 1]) continue

        // All remaining pivots are ≥ sorted[i] > 0 → sum can never be 0
        if (sorted[i] > 0) break

        var low = i + 1
        var high = sorted.lastIndex

        while (low < high) {
            val sum = sorted[i] + sorted[low] + sorted[high]
            when {
                sum == 0 -> {
                    result.add(listOf(sorted[i], sorted[low], sorted[high]))
                    while (low < high && sorted[low] == sorted[low + 1]) low++   // skip low dupes
                    while (low < high && sorted[high] == sorted[high - 1]) high-- // skip high dupes
                    low++; high--  // move past the matched pair
                }
                sum < 0 -> low++   // need larger value
                else    -> high--  // need smaller value
            }
        }
    }

    return result
}
```

### Step-by-Step Walkthrough

```
sorted = [-4, -1, -1, 0, 1, 2]
          0    1   2  3  4  5

── i=0, pivot=-4 ─────────────────────────────────────────
  i=0: skip check (i > 0 is false)
  sorted[0]=-4, not > 0 → no break
  low=1, high=5
    sum = -4 + -1 + 2 = -3  < 0 → low++
    sum = -4 + -1 + 2 = -3  < 0 → low++   (low=2, same value)
    sum = -4 +  0 + 2 = -2  < 0 → low++
    sum = -4 +  1 + 2 = -1  < 0 → low++
    low=5 == high=5 → done, no hit

── i=1, pivot=-1 ─────────────────────────────────────────
  i=1 > 0, sorted[1]=-1 == sorted[0]=-4? NO → proceed
  sorted[1]=-1, not > 0 → no break
  low=2, high=5
    sum = -1 + -1 + 2 = 0  ✓ → add [-1,-1,2]
      skip low dupes:  sorted[2]=-1, sorted[3]=0  → not equal → no skip
      skip high dupes: sorted[5]=2,  sorted[4]=1  → not equal → no skip
      low++ → low=3,  high-- → high=4
    sum = -1 + 0 + 1 = 0  ✓ → add [-1,0,1]
      skip low dupes:  sorted[3]=0,  sorted[4]=1  → not equal → no skip
      skip high dupes: sorted[4]=1,  sorted[3]=0  → not equal → no skip
      low++ → low=4,  high-- → high=3
    low=4 > high=3 → done

── i=2, pivot=-1 ─────────────────────────────────────────
  i=2 > 0, sorted[2]=-1 == sorted[1]=-1? YES → CONTINUE (skip entire scan)
  ← This is where Approach 1 wasted work; here we skip instantly

── i=3, pivot=0 ──────────────────────────────────────────
  i=3 > 0, sorted[3]=0 == sorted[2]=-1? NO → proceed
  sorted[3]=0, not > 0 → no break
  low=4, high=5
    sum = 0 + 1 + 2 = 3  > 0 → high--
    low=4 == high=4 → done, no hit

── i=4 → lastIndex - 2 = 3, loop ends ───────────────────

result = [[-1,-1,2], [-1,0,1]]
```

### The Three Duplicate Guards — Visualised

```
sorted = [-1, -1, -1,  0,  0,  1,  2]
          i0   i1  i2  L0  L1       H

Guard 1 — Outer skip:
  i=0: process normally, finds triplets
  i=1: sorted[1] == sorted[0] → SKIP (would re-fix same pivot)
  i=2: sorted[2] == sorted[1] → SKIP

Guard 2 — Inner low skip (after a hit at low=L0):
  If sorted[L0] == sorted[L0+1], advancing low by 1 would re-pair same value
  → keep advancing until sorted[low] != sorted[low+1], then do the final low++

Guard 3 — Inner high skip (after a hit at high=H):
  Same logic on the right side for sorted[high]
```

### The Early Break

```
sorted = [1, 2, 3, 4, 5]

i=0, pivot=1 > 0
→ break immediately. Remaining pivots are 2,3,4,5 — all positive.
   Two elements to the right are also positive (sorted array).
   a + b + c ≥ 1 + 2 + 3 = 6 > 0. No solution exists.
```

### Complexity

| | |
|---|---|
| **Time** | O(n²) — O(n log n) sort + O(n²) two-pointer scan |
| **Space** | O(1) — no auxiliary structure (output list doesn't count) |

---

## Side-by-Side Comparison

```
sorted = [-1, -1, -1, 0, 1, 2]   (three -1s to stress dedup)

Approach 1 (HashSet):
  i=0 pivot=-1 → scans fully → finds [-1,0,1] → adds to set
  i=1 pivot=-1 → scans fully → finds [-1,0,1] → SET REJECTS (hash collision)
  i=2 pivot=-1 → scans fully → finds [-1,0,1] → SET REJECTS again
  Total inner iterations: ~3 × O(n) scans

Approach 2 (Skip):
  i=0 pivot=-1 → scans fully → finds [-1,0,1]
  i=1 pivot=-1 → sorted[1]==sorted[0] → CONTINUE instantly
  i=2 pivot=-1 → sorted[2]==sorted[1] → CONTINUE instantly
  Total inner iterations: ~1 × O(n) scan
```

Same asymptotic complexity, but Approach 2 eliminates redundant work and allocates zero auxiliary objects.

---

## Decision Guide

| Situation | Use |
|---|---|
| Contest / throwaway code, clarity matters | Approach 1 — simpler, harder to get wrong |
| Production / interview optimal solution | Approach 2 — O(1) space, no hidden allocations |
| Array has many repeated values | Approach 2 — duplicate skipping saves real work |
| Array has all unique values | Both equivalent in practice |
