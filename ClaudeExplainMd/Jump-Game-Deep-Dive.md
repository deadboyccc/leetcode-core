# LeetCode 55 – Jump Game: Complete Study Reference

> **Problem**: Given an integer array `nums`, where `nums[i]` is the maximum jump length from index `i`,
> return `true` if you can reach the last index starting from index 0.

---

## Table of Contents

1. [Problem Breakdown](#1-problem-breakdown)
2. [Solution 1 — Brute-Force Recursion](#2-solution-1--brute-force-recursion)
3. [Solution 2 — Top-Down DP (Memoized Recursion)](#3-solution-2--top-down-dp-memoized-recursion)
4. [Solution 3 — Bottom-Up DP (Tabulation)](#4-solution-3--bottom-up-dp-tabulation)
5. [Solution 4 — Greedy Forward Scan](#5-solution-4--greedy-forward-scan)
6. [Solution 5 — Greedy Reverse (Goal Shrinking)](#6-solution-5--greedy-reverse-goal-shrinking)
7. [Complete Code File](#7-complete-code-file)
8. [Complexity Cheat Sheet](#8-complexity-cheat-sheet)
9. [Mental Model & Progression](#9-mental-model--progression)
10. [Edge Cases](#10-edge-cases)

---

## 1. Problem Breakdown

```
Input:  nums = [2, 3, 1, 1, 4]
                0  1  2  3  4   ← indices

At index 0: nums[0]=2, so you can jump to index 1 or 2
At index 1: nums[1]=3, so you can jump to index 2, 3, or 4
...

Goal: can any sequence of jumps land you on index 4?
Answer: true  (e.g. 0 → 1 → 4)
```

```
Input:  nums = [3, 2, 1, 0, 4]
                0  1  2  3  4

Every path funnels through index 3, where nums[3]=0.
You are stuck. No path can escape index 3.
Answer: false
```

**Key rules:**
- `nums[i]` is a *maximum* — you can jump any amount from 1 to `nums[i]`
- `nums[i] = 0` means you are completely stuck at that index
- The question is purely binary: reachable or not (not minimum jumps)

---

## 2. Solution 1 — Brute-Force Recursion

### Intuition

Model the problem exactly as stated. Stand at index 0. Try every possible jump.
For each landing spot, recurse and try every jump from there.
If any path ever reaches the last index, return `true`.

This is a **decision tree** — at each node you branch into every valid next position.

### Visualization

```
nums = [2, 3, 1, 1, 4]

dfs(0)  — from index 0, can jump 1 or 2 steps
  ├── dfs(1)  — from index 1, can jump 1, 2, or 3 steps
  │     ├── dfs(2)
  │     │     └── dfs(3)
  │     │           └── dfs(4) ✅ reached end!
  │     ├── dfs(3)
  │     │     └── dfs(4) ✅
  │     └── dfs(4) ✅
  └── dfs(2)
        └── dfs(3)
              └── dfs(4) ✅

Notice: dfs(2), dfs(3), dfs(4) each appear MULTIPLE TIMES.
This redundancy is the problem — exponential work.
```

### Code With Line-by-Line Commentary

```kotlin
// Brute-force: try every possible jump path.
// T: O(2^n)  S: O(n) stack depth
fun canJumpBrute(nums: IntArray): Boolean {
    val n = nums.size
    // Capture n in the outer scope so the nested function can see it.
    // Kotlin nested functions close over their enclosing scope freely.

    fun dfs(idx: Int): Boolean {
        // BASE CASE: if idx has reached or passed the last index, we win.
        // "passed" handles cases where a jump overshoots the end — still valid.
        if (idx >= n - 1) return true

        // COMPUTE REACH: from this index, the farthest we can jump is idx + nums[idx].
        // minOf clamps this to n-1 so we never generate out-of-bounds indices
        // in the loop below. Without this clamp, the range (idx+1..maxReach)
        // could produce indices beyond the array and cause IndexOutOfBoundsException.
        val maxReach = minOf(idx + nums[idx], n - 1)

        // TRY EVERY JUMP: iterate every landing spot from idx+1 to maxReach.
        // `any { }` is Kotlin's short-circuit "or" over a range — it calls dfs(it)
        // for each value and stops immediately when the first `true` is returned.
        // This means we don't explore siblings once one succeeds.
        return (idx + 1..maxReach).any { dfs(it) }
    }

    // Start the recursion from index 0.
    return dfs(0)
}
```

### Why It's Slow

```
nums = [5, 4, 3, 2, 1, 0]

dfs(0) tries indices 1,2,3,4,5
  dfs(1) tries indices 2,3,4,5
    dfs(2) tries indices 3,4,5
      ...

dfs(3) is computed from dfs(0), dfs(1), AND dfs(2).
Same work, done 3 times. In a long array this becomes 2^n.
```

---

## 3. Solution 2 — Top-Down DP (Memoized Recursion)

### Intuition

The brute-force has one fatal flaw: it recomputes the answer for the same index
multiple times. But the answer for index `i` is **deterministic** — it depends only
on `nums[i]` and the answers to the right of it, which never change.

So: compute it once, store the result, return instantly on repeat visits.
This is **memoization** — a cache layered on top of identical recursive logic.

### Visualization

```
nums = [2, 3, 1, 1, 4]

First encounter of dfs(2):
  → computes: can I reach end from 2? Yes → memo[2] = true

Second encounter of dfs(2) (from a different call path):
  → memo[2] exists → return true immediately. No recursion.

The pruned tree:

dfs(0)
  ├── dfs(1)
  │     ├── dfs(2) ← computed here, memo[2]=true ✅
  │     ├── dfs(3) ← memo hit, skip
  │     └── dfs(4) ← memo hit, skip
  └── dfs(2) ← memo hit, return true instantly ✅

Each index is computed at most once.
```

### Code With Line-by-Line Commentary

```kotlin
// Top-down DP: same recursion as brute-force, but with a memo cache.
// T: O(n²)  S: O(n) memo + O(n) stack
fun canJumpMemo(nums: IntArray): Boolean {
    val n = nums.size

    // HashMap<Int, Boolean>: maps index → can it reach the end?
    // We use a HashMap rather than an array because Kotlin's `?.let` idiom
    // on a nullable value reads more cleanly than array sentinel checks.
    val memo = HashMap<Int, Boolean>()

    fun dfs(idx: Int): Boolean {
        // BASE CASE: same as brute-force.
        if (idx >= n - 1) return true

        // CACHE HIT: `memo[idx]` returns null if key absent, or Boolean if present.
        // `?.let { return it }` means: if non-null, execute the lambda and return.
        // The `return` inside `let` is a non-local return — it exits `dfs`, not just the lambda.
        // This is idiomatic Kotlin for "early return on cache hit".
        memo[idx]?.let { return it }

        val maxReach = minOf(idx + nums[idx], n - 1)

        // Identical to brute-force — try every jump, short-circuit on first success.
        val result = (idx + 1..maxReach).any { dfs(it) }

        // CACHE WRITE: `.also { }` executes its lambda with the receiver as `it`,
        // then returns the receiver unchanged. So `result.also { memo[idx] = it }`
        // stores result in the memo AND returns result — all in one expression.
        // This avoids writing: memo[idx] = result; return result
        return result.also { memo[idx] = it }
    }

    return dfs(0)
}
```

### Cache Hit Mechanics

```
memo[idx]?.let { return it }

Step by step:
  1. memo[idx]          → returns Boolean? (null if not cached)
  2. ?.let { ... }      → skips the block entirely if null (safe call)
  3. { return it }      → if non-null, `it` is the cached Boolean; return it from dfs
  4. If null, fall through and compute normally
```

---

## 4. Solution 3 — Bottom-Up DP (Tabulation)

### Intuition

Memoized recursion works top-down: start at 0, drill down, answers bubble back up.
Tabulation flips this: **fill the answer table from the known end backwards to the unknown start**.

Since we fill right-to-left, when we compute `dp[idx]`, every index to its right
is already computed. No recursion needed — pure iteration.

### Visualization

```
nums  = [ 2,  3,  1,  1,  4]
index =   0   1   2   3   4

Initial:
dp    = [ F,  F,  F,  F,  T]   ← dp[n-1] = true (base case)

idx=3: maxReach = 3 + nums[3] = 3 + 1 = 4
       check dp[4] = true  → dp[3] = true
dp    = [ F,  F,  F,  T,  T]

idx=2: maxReach = 2 + nums[2] = 2 + 1 = 3
       check dp[3] = true  → dp[2] = true
dp    = [ F,  F,  T,  T,  T]

idx=1: maxReach = 1 + nums[1] = 1 + 3 = 4
       check dp[2], dp[3], dp[4] — dp[2] = true → dp[1] = true
dp    = [ F,  T,  T,  T,  T]

idx=0: maxReach = 0 + nums[0] = 0 + 2 = 2
       check dp[1], dp[2] — dp[1] = true → dp[0] = true
dp    = [ T,  T,  T,  T,  T]

return dp[0] = true ✅
```

```
Failing example: nums = [3, 2, 1, 0, 4]

dp    = [ F,  F,  F,  F,  T]

idx=3: maxReach = 3 + 0 = 3 → range (4..3) is EMPTY → dp[3] = false
dp    = [ F,  F,  F,  F,  T]

idx=2: maxReach = 2 + 1 = 3 → check dp[3] = false → dp[2] = false
idx=1: maxReach = 1 + 2 = 3 → check dp[2], dp[3] = false, false → dp[1] = false
idx=0: maxReach = 0 + 3 = 3 → check dp[1..3] = false, false, false → dp[0] = false

return dp[0] = false ✅
```

### Code With Line-by-Line Commentary

```kotlin
// Bottom-up DP: fill the answer table right-to-left, no recursion.
// T: O(n²)  S: O(n)
fun canJumpDP(nums: IntArray): Boolean {
    val n = nums.size

    // BooleanArray(n) { false }: creates an array of size n, all initialized to false.
    // The lambda `{ false }` is the initializer — called once per index.
    // Alternative: BooleanArray(n) (defaults to false anyway, but explicit is clearer).
    val dp = BooleanArray(n) { false }

    // BASE CASE: the last index can always "reach itself".
    // Everything else is false until proven reachable.
    dp[n - 1] = true

    // Fill right-to-left. `downTo` creates a decreasing IntRange.
    // We stop at 0 (inclusive). We start at n-2 because n-1 is already set.
    for (idx in n - 2 downTo 0) {

        // Same clamped reach as before.
        // minOf prevents the inner range from exceeding array bounds.
        val maxReach = minOf(idx + nums[idx], n - 1)

        // Check if ANY index in (idx+1..maxReach) is already marked true.
        // `any { dp[it] }` short-circuits on first true — identical semantics
        // to the recursive version, just without a call stack.
        // If nums[idx] == 0, then maxReach == idx, and (idx+1..idx) is an
        // empty range. `any` on an empty range returns false automatically.
        dp[idx] = (idx + 1..maxReach).any { dp[it] }
    }

    // Index 0 is reachable from itself by definition; the question is
    // whether it can chain to the end.
    return dp[0]
}
```

### Empty Range Behaviour

```kotlin
// When nums[idx] == 0:
val maxReach = minOf(idx + 0, n - 1) = idx
// Range: (idx + 1..idx) which is e.g. (3..2) — empty
// any { } on empty range → false (correct: stuck, can't jump anywhere)
```

---

## 5. Solution 4 — Greedy Forward Scan

### Intuition

The DP solutions track *which* indices can reach the end — a full boolean table.
But do we need all that? Consider what we actually care about:

> Can I reach index `i`?

All that matters is: **has any previously reachable index been able to stretch far enough to cover index `i`?**

Track a single variable `maxReach` — the farthest index reachable from anything
we've visited so far. Walk forward. At each step:
- If `idx > maxReach`: we've walked past our reach. Stuck. Return false.
- Otherwise: extend `maxReach` if jumping from here reaches farther.

### Visualization

```
nums  = [ 2,  3,  1,  1,  4]
index =   0   1   2   3   4
maxReach starts at 0

idx=0: 0 <= maxReach(0) ✅  maxReach = max(0, 0+2) = 2
       "From here I can reach up to index 2"
       ──────────────────────────────
       [0][ ][ ][ ][ ]
        ^──────^       frontier=2

idx=1: 1 <= maxReach(2) ✅  maxReach = max(2, 1+3) = 4
       ──────────────────────────────
       [0][1][ ][ ][ ]
           ^──────────^ frontier=4

idx=2: 2 <= maxReach(4) ✅  maxReach = max(4, 2+1) = 4 (no change)
idx=3: 3 <= maxReach(4) ✅  maxReach = max(4, 3+1) = 4 (no change)
idx=4: 4 <= maxReach(4) ✅  maxReach = max(4, 4+4) = 8 (clamped by loop end)

Loop ends normally → return true ✅
```

```
Failing: nums = [3, 2, 1, 0, 4]

idx=0: maxReach = max(0, 0+3) = 3
idx=1: maxReach = max(3, 1+2) = 3
idx=2: maxReach = max(3, 2+1) = 3
idx=3: maxReach = max(3, 3+0) = 3  ← stuck at 3, frontier can't grow
idx=4: 4 > maxReach(3) ❌ → return false
```

### Code With Line-by-Line Commentary

```kotlin
// Greedy forward scan: track the farthest reachable index.
// T: O(n)  S: O(1)
fun canJumpGreedy(nums: IntArray): Boolean {

    // maxReach: the farthest index reachable from any index we've visited so far.
    // Starts at 0 because from index 0, we haven't made any jumps yet —
    // index 0 itself is the only guaranteed reachable position.
    var maxReach = 0

    // `nums.indices` is shorthand for `0 until nums.size` — idiomatic Kotlin.
    // We iterate every index in order.
    for (idx in nums.indices) {

        // STUCK CHECK: if the current index is beyond our frontier,
        // no previously reachable index could jump here. We're isolated.
        // Return false immediately — no need to continue.
        if (idx > maxReach) return false

        // EXTEND FRONTIER: if jumping from idx reaches farther than our
        // current frontier, update it. `idx + nums[idx]` is the farthest
        // single jump we can make from here.
        // maxOf keeps the best (farthest) reach seen so far.
        maxReach = maxOf(maxReach, idx + nums[idx])
    }

    // If the loop completes without returning false, every index was reachable.
    // The last index was traversed successfully.
    return true
}
```

### Why We Don't Need to Clamp maxReach Here

In the DP solutions, we clamped `maxReach = minOf(idx + nums[idx], n - 1)` to
avoid out-of-bounds array access. Here, we never index into an array with
`maxReach` — we only compare it against `idx`. So overshooting `n-1` is
harmless; if `maxReach >= n-1`, the loop will complete and return `true`.

---

## 6. Solution 5 — Greedy Reverse (Goal Shrinking)

### Intuition

The forward greedy asks: *"how far forward can I push my frontier?"*
The reverse greedy asks: *"can I pull the finish line backwards all the way to start?"*

Start with `goal = n - 1` (must reach the last index).
Walk right-to-left. At each index, ask: *can this index reach the current goal in one jump?*
If yes, this index **becomes** the new goal — because any path reaching this index
can chain to the end.
If `goal` eventually reaches 0, then index 0 can chain to the end. Win.

### Visualization

```
nums  = [ 2,  3,  1,  1,  4]
index =   0   1   2   3   4

goal = 4  (must reach index 4)

idx=3: 3 + nums[3] = 3 + 1 = 4 >= goal(4) → goal = 3
       "Index 3 can reach index 4, so index 3 is our new goal"

idx=2: 2 + nums[2] = 2 + 1 = 3 >= goal(3) → goal = 2
idx=1: 1 + nums[1] = 1 + 3 = 4 >= goal(2) → goal = 1
idx=0: 0 + nums[0] = 0 + 2 = 2 >= goal(1) → goal = 0

goal == 0 → return true ✅
```

```
Failing: nums = [3, 2, 1, 0, 4]

goal = 4

idx=3: 3 + 0 = 3 < goal(4) → goal stays 4
idx=2: 2 + 1 = 3 < goal(4) → goal stays 4
idx=1: 1 + 2 = 3 < goal(4) → goal stays 4
idx=0: 0 + 3 = 3 < goal(4) → goal stays 4

goal = 4 ≠ 0 → return false ✅
```

### Code With Line-by-Line Commentary

```kotlin
// Greedy reverse: shrink the goal backwards until (or unless) it hits 0.
// T: O(n)  S: O(1)
fun canJumpGreedyReverse(nums: IntArray): Boolean {

    // goal: the leftmost index that still needs to be "proven reachable".
    // Initially the last index — that's what we need to reach.
    var goal = nums.size - 1

    // Walk right-to-left, stopping before the last index (it's already the goal).
    // `downTo` creates a decreasing range.
    for (idx in nums.size - 2 downTo 0) {

        // Can this index reach the current goal in one jump?
        // `idx + nums[idx]` is the farthest this index can jump.
        // If it's >= goal, then this index can reach the goal.
        // So we don't need to prove the old goal separately —
        // proving we can reach THIS index is now sufficient.
        if (idx + nums[idx] >= goal) goal = idx
    }

    // If goal was pulled all the way back to 0, index 0 can chain to the end.
    // If goal is still > 0, there's a gap that can't be crossed.
    return goal == 0
}
```

### Forward vs Reverse Greedy — Two Sides of the Same Coin

```
Forward: push a frontier rightward
  maxReach grows as we find farther-jumping indices
  Fail when frontier can't keep up with our position

Reverse: pull a goal leftward
  goal shrinks as we find indices that can reach it
  Fail when goal never reaches 0

Both are O(n) O(1). Choose whichever matches your mental model.
```

---

## 7. Complete Code File

```kotlin
// ============================================================
// LeetCode 55 – Jump Game
// Can you reach the last index from index 0?
// nums[i] = max jump length from index i
// ============================================================


// ── 1. Brute-force recursion (no memo) ──────────────────────
// Try every possible jump from each index.
// T: O(2^n)  S: O(n) stack
fun canJumpBrute(nums: IntArray): Boolean {
    val n = nums.size

    fun dfs(idx: Int): Boolean {
        // Base case: reached or passed the last index
        if (idx >= n - 1) return true
        // Clamp reach to avoid out-of-bounds range values
        val maxReach = minOf(idx + nums[idx], n - 1)
        // Try every jump; stop as soon as one path succeeds
        return (idx + 1..maxReach).any { dfs(it) }
    }

    return dfs(0)
}


// ── 2. Top-down DP (memoized recursion) ─────────────────────
// Same recursion but cache each index's answer.
// T: O(n²)  S: O(n)
fun canJumpMemo(nums: IntArray): Boolean {
    val n = nums.size
    val memo = HashMap<Int, Boolean>()

    fun dfs(idx: Int): Boolean {
        if (idx >= n - 1) return true
        // Return cached answer if available (non-local return from dfs)
        memo[idx]?.let { return it }
        val maxReach = minOf(idx + nums[idx], n - 1)
        val result = (idx + 1..maxReach).any { dfs(it) }
        // Store and return in one expression
        return result.also { memo[idx] = it }
    }

    return dfs(0)
}


// ── 3. Bottom-up DP (tabulation) ────────────────────────────
// Fill dp right-to-left: dp[i] = can index i reach the end?
// T: O(n²)  S: O(n)
fun canJumpDP(nums: IntArray): Boolean {
    val n = nums.size
    val dp = BooleanArray(n) { false }
    // Base case: last index can reach itself
    dp[n - 1] = true

    for (idx in n - 2 downTo 0) {
        val maxReach = minOf(idx + nums[idx], n - 1)
        // true if any reachable neighbour can reach the end
        // empty range (nums[idx]==0) returns false automatically
        dp[idx] = (idx + 1..maxReach).any { dp[it] }
    }

    return dp[0]
}


// ── 4. Greedy forward scan (optimal) ────────────────────────
// Track the farthest index reachable so far.
// If we ever stand on an index beyond that frontier, we're stuck.
// T: O(n)  S: O(1)
fun canJumpGreedy(nums: IntArray): Boolean {
    // Farthest index any visited position can reach
    var maxReach = 0

    for (idx in nums.indices) {
        // Current position is beyond our reach — unreachable
        if (idx > maxReach) return false
        // Extend frontier if this position jumps farther
        maxReach = maxOf(maxReach, idx + nums[idx])
    }

    return true
}


// ── 5. Greedy reverse (goal shrinking) ──────────────────────
// Walk right-to-left; pull the goal backward whenever a position
// can directly reach it. If goal reaches 0, index 0 chains to the end.
// T: O(n)  S: O(1)
fun canJumpGreedyReverse(nums: IntArray): Boolean {
    // Start: must reach the last index
    var goal = nums.size - 1

    for (idx in nums.size - 2 downTo 0) {
        // If this index can reach the current goal, it becomes the new goal
        if (idx + nums[idx] >= goal) goal = idx
    }

    // If goal was pulled to 0, the start can chain all the way to the end
    return goal == 0
}
```

---

## 8. Complexity Cheat Sheet

| Solution | Time | Space | Notes |
|---|---|---|---|
| Brute-force recursion | O(2ⁿ) | O(n) | Call stack depth |
| Memoized recursion | O(n²) | O(n) | Map + call stack |
| Tabulation | O(n²) | O(n) | dp array, no stack |
| Greedy forward | O(n) | O(1) | Single variable |
| Greedy reverse | O(n) | O(1) | Single variable |

**Why O(n²) for DP?**
- Outer loop: O(n) iterations
- Inner `any { }` per iteration: up to O(n) checks in the worst case
- Total: O(n) × O(n) = O(n²)

**Why O(n) for Greedy?**
- No inner loop — one arithmetic operation and one comparison per index
- `maxOf` and `>=` are O(1)

---

## 9. Mental Model & Progression

```
QUESTION EACH SOLUTION ASKS:
────────────────────────────────────────────────────────────────

Brute Force   "What are ALL the paths? Is any of them valid?"
              → Builds a full decision tree. Recomputes everything.

+ Memo        "Same question, but remember what we already know."
              → Same tree, cached nodes. Cuts exponential to polynomial.

Tabulation    "What is the answer for EVERY index, built from known answers?"
              → Fills the table iteratively. Equivalent to memo, no stack.

Greedy Fwd    "What is the FARTHEST I can possibly reach at each step?"
              → Discards individual index answers. One frontier number suffices.

Greedy Rev    "Can I PULL THE GOAL back to the start?"
              → Mirror image of forward. Equally minimal.
```

```
INFORMATION DISCARDED AT EACH STEP:
────────────────────────────────────────────────────────────────

Brute Force   keeps nothing        → recomputes everything
+ Memo        keeps per-index bool → avoids recomputation
Tabulation    keeps per-index bool → avoids call stack overhead
Greedy Fwd    keeps ONE number     → discards individual index answers
Greedy Rev    keeps ONE number     → same, different direction
```

The progression is a lesson in **identifying what information you actually need**.
Each optimization throws away something the previous solution was tracking unnecessarily.

---

## 10. Edge Cases

```kotlin
// Single element — already at the end
nums = [0]  → true   // idx=0 >= n-1=0, base case fires immediately

// All zeros except start
nums = [1, 0, 0]  → false  // can reach index 1 but stuck there

// Large jump at start overshoots
nums = [100, 0, 0]  → true  // maxReach jumps to 100, covers everything

// Exact reach
nums = [1, 1, 0]  → true   // 0→1→2, just barely reaches the end

// Zero at start
nums = [0, 1]  → false  // can't move from index 0 at all
// Greedy: idx=0, 0 <= maxReach(0) ✅, maxReach = max(0, 0+0) = 0
//         idx=1, 1 > maxReach(0) ❌ → false
```
