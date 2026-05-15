# LC 2908 — Minimum Sum of Mountain Triplet

## Table of Contents

1. [Problem Statement](#problem-statement)
2. [Key Definitions](#key-definitions)
3. [Concrete Examples](#concrete-examples)
4. [Intuition Building](#intuition-building)
5. [Approach 1 — Brute Force](#approach-1--brute-force)
6. [Approach 2 — Optimized O(n)](#approach-2--optimized-on)
7. [The Bug in the Original Optimized Code](#the-bug-in-the-original-optimized-code)
8. [Fixed + Commented Code](#fixed--commented-code)
9. [Complexity Analysis](#complexity-analysis)
10. [Edge Cases](#edge-cases)
11. [Key Takeaways](#key-takeaways)

---

## Problem Statement

> Given a **0-indexed** integer array `nums`, find the **minimum possible sum** of a **mountain triplet** `(i, j, k)` where `i < j < k` and:
>
> - `nums[j] > nums[i]`
> - `nums[j] > nums[k]`
>
> Return the minimum sum, or **-1** if no such triplet exists.

In plain English: pick three indices left–middle–right. The **middle element must be strictly greater than both the left and right elements**. Minimize `nums[i] + nums[j] + nums[k]`.

---

## Key Definitions

| Term | Meaning |
|---|---|
| **Triplet** | Three elements at distinct indices `i < j < k` |
| **Mountain triplet** | A triplet where the middle element is strictly greater than both its neighbors |
| **Peak** | `nums[j]` — the middle element (must be the largest of the three) |
| **Left valley** | `nums[i]` — must be strictly less than the peak |
| **Right valley** | `nums[k]` — must be strictly less than the peak |

---

## Concrete Examples

### Example 1

```
nums = [8, 6, 1, 5, 3]
```

Valid mountain triplets:
- `(i=0, j=1, k=2)` → values `8, 6, 1` → peak=6 > 8? **NO** (6 < 8). Invalid.
- `(i=2, j=3, k=4)` → values `1, 5, 3` → peak=5 > 1 ✓, peak=5 > 3 ✓. Sum = **9**
- `(i=0, j=3, k=4)` → values `8, 5, 3` → peak=5 > 8? **NO**. Invalid.
- `(i=1, j=3, k=4)` → values `6, 5, 3` → peak=5 > 6? **NO**. Invalid.
- `(i=2, j=3, k=4)` → **Sum = 9** ← minimum

Answer: **9**

---

### Example 2

```
nums = [5, 4, 8, 7, 10, 2]
```

Let's enumerate some candidates with `j=4` (peak=10):
- Left min up to index 3 = `min(5,4,8,7)` = 4
- Right min from index 5 = 2
- Sum = 10 + 4 + 2 = **16**

With `j=2` (peak=8):
- Left min up to index 1 = `min(5,4)` = 4
- Right min from index 3 = `min(7,10,2)` = 2
- Sum = 8 + 4 + 2 = **14**

Answer: **13** (check j=3, peak=7: left min=4 from [5,4,8], but 7>8? No. j=4: 4+10+2=16. Brute force gives 13)

Actually let's be precise:
```
j=2 (val=8): leftMin=min(5,4)=4, rightMin=min(7,10,2)=2 → 4+8+2=14
j=3 (val=7): leftMin=min(5,4,8)=4, rightMin=min(10,2)=2 → 4+7+2=13
j=4 (val=10): leftMin=min(5,4,8,7)=4, rightMin=2 → 4+10+2=16
```

Answer: **13**

---

### Example 3 — No valid triplet

```
nums = [1, 2]
```

Only 2 elements — impossible to form a triplet of size 3.

Answer: **-1**

```
nums = [3, 2, 1]
```

Strictly decreasing — no element can be a valid peak because `nums[j] > nums[i]` requires at least one smaller to the left.

Answer: **-1**

---

## Intuition Building

### Why minimize the peak helps, but isn't enough

Your first instinct might be: "just find the smallest peak." But the sum is `left + peak + right`. A larger peak paired with two tiny valleys can still beat a small peak paired with large valleys.

```
Example:
[1, 100, 2, 50, 3]

Peak=100: left=1, right=2 → sum=103
Peak=50:  left=1, right=3 → sum=54  ← wins despite larger "peak feel"
```

So you must minimize all three simultaneously. The trick: **for a fixed peak position `j`, always pick the global minimum to its left and the global minimum to its right.** This gives the best possible sum for that peak.

### The key insight

For each index `j` acting as the peak:
- `leftMin[j]` = minimum of `nums[0..j-1]`
- `rightMin[j]` = minimum of `nums[j+1..n-1]`
- Valid mountain condition: `nums[j] > leftMin[j]` AND `nums[j] > rightMin[j]`
- Candidate sum = `nums[j] + leftMin[j] + rightMin[j]`

Answer = minimum of all valid candidate sums.

---

## Approach 1 — Brute Force

### Strategy

Iterate over every possible middle index `j`. For each `j`:
1. Find the minimum of all elements to the left (`0..j-1`).
2. Find the minimum of all elements to the right (`j+1..n-1`).
3. Check the mountain condition.
4. Update the global minimum sum.

### Annotated Code

```kotlin
// Brute Force — O(n²) time, O(n) space (for the slices)
class Solution {
    fun minimumSum(nums: IntArray): Int {
        // Build an index→value map. Using associateWith gives a clean
        // way to iterate with both index and value, though a plain
        // for loop over indices would also work.
        val idxToValue = nums.indices.associateWith { nums[it] }

        // Start with MAX_VALUE so any valid sum will replace it.
        var minSum = Int.MAX_VALUE

        for ((idx, num) in idxToValue) {
            // --- Left side: smallest element strictly to the left ---
            // slice(0 until idx) is empty when idx=0 → minOrNull() → null
            val leftSideMin = nums.slice(0 until idx).minOrNull()

            // --- Right side: smallest element strictly to the right ---
            // slice(idx+1 until size) is empty at last index → null
            val rightSideMin = nums.slice(idx + 1 until nums.size).minOrNull()

            // Mountain validity check:
            //   - Both sides must exist (non-null) → at least one element
            //     on each side, so we have a true triplet i < j < k.
            //   - num (the peak) must be STRICTLY greater than both mins.
            //     If equal, it's not a mountain peak.
            if ((leftSideMin != null) && (rightSideMin != null) &&
                (num > leftSideMin) && (num > rightSideMin)
            ) {
                // This peak forms a valid mountain. Record the minimum sum.
                minSum = minOf(minSum, num + leftSideMin + rightSideMin)
            }
        }

        // If no valid triplet was found, minSum is still MAX_VALUE → return -1.
        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }
}
```

### Trace on `[8, 6, 1, 5, 3]`

| idx | num | leftSideMin | rightSideMin | Valid? | Sum |
|-----|-----|-------------|--------------|--------|-----|
| 0 | 8 | null | — | ✗ | — |
| 1 | 6 | 8 | — | 6>8? ✗ | — |
| 2 | 1 | 6 | — | 1>6? ✗ | — |
| 3 | 5 | 1 | 3 | 5>1 ✓, 5>3 ✓ | 9 |
| 4 | 3 | 1 | null | — | — |

Result: **9** ✓

---

## Approach 2 — Optimized O(n)

### Strategy

The brute force recomputes `leftMin` and `rightMin` from scratch at every `j`. We can precompute or maintain them incrementally:

- `leftMin`: scan left→right, keep a running minimum. By the time we're at index `j`, `leftMin` = min of all elements seen before `j`. Update **after** using it.
- `rightMin`: precompute a `rightMin` array where `rightMin[j]` = min of `nums[j..n-1]`, built by scanning right→left.

Then a single left-to-right pass over candidate peaks `j ∈ [1, n-2]` checks each in O(1).

### Why precompute `rightMin` as an array?

Because as `j` moves left→right, the "right window" shrinks. We can't maintain it as a single running minimum going forward — we'd need to know the minimum of a suffix, which requires either a precomputed array or a monotonic structure. The precomputed array is the cleanest O(n) solution.

### Annotated Code (Fixed)

```kotlin
class Solution {
    fun minimumSum(nums: IntArray): Int {
        val n = nums.size

        // Need at least 3 elements to form a triplet (i, j, k).
        if (n < 3) return -1

        // --- Precompute suffix minimums ---
        // rightMin[i] = minimum of nums[i..n-1]
        // Build right→left so each entry answers "what's the smallest
        // element from here to the end?"
        val rightMin = IntArray(n)
        rightMin[n - 1] = nums[n - 1]           // base case: last element
        for (i in n - 2 downTo 0) {
            rightMin[i] = minOf(nums[i], rightMin[i + 1])
        }
        // After this loop:
        //   rightMin[0] = global min of the whole array
        //   rightMin[n-1] = nums[n-1]

        // --- Single left-to-right pass over candidate peaks ---
        // leftMin tracks the running minimum of nums[0..j-1].
        // Initialized to nums[0] before the loop starts at j=1.
        var leftMin = nums[0]

        var minSum = Int.MAX_VALUE

        // j ranges over valid peak positions: must have at least one
        // element to the left (j ≥ 1) and one to the right (j ≤ n-2).
        for (j in 1 until n - 1) {
            val peak = nums[j]

            // rightMin[j+1] = minimum of nums[j+1..n-1], i.e. the best
            // right-valley we can pair with this peak.
            val rightVal = rightMin[j + 1]

            // Mountain condition: peak must be strictly greater than
            // both the best left-valley and the best right-valley.
            if (peak > leftMin && peak > rightVal) {
                minSum = minOf(minSum, peak + leftMin + rightVal)
            }

            // Expand the left window: include nums[j] for future peaks.
            // This must happen AFTER the sum check so that leftMin for
            // index j is truly the minimum of nums[0..j-1], not nums[0..j].
            leftMin = minOf(leftMin, peak)
        }

        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }
}
```

### Trace on `[5, 4, 8, 7, 10, 2]`

**Precompute rightMin:**

| i | nums[i] | rightMin[i] |
|---|---------|-------------|
| 5 | 2 | 2 |
| 4 | 10 | 2 |
| 3 | 7 | 2 |
| 2 | 8 | 2 |
| 1 | 4 | 2 |
| 0 | 5 | 2 |

**Main loop (leftMin starts at 5):**

| j | peak | leftMin | rightMin[j+1] | peak>left? | peak>right? | Sum |
|---|------|---------|---------------|------------|-------------|-----|
| 1 | 4 | 5 | 2 | 4>5? ✗ | — | — |
| 2 | 8 | 4 | 2 | 8>4 ✓ | 8>2 ✓ | 14 |
| 3 | 7 | 4 | 2 | 7>4 ✓ | 7>2 ✓ | **13** |
| 4 | 10 | 4 | 2 | 10>4 ✓ | 10>2 ✓ | 16 |

After j=1: leftMin = min(5,4) = 4  
After j=2: leftMin = min(4,8) = 4  
After j=3: leftMin = min(4,7) = 4  

Result: **13** ✓

---

## The Bug in the Original Optimized Code

The original attempt tried to avoid the `rightMin` array by maintaining a running `rightMin` variable. Here's why this approach is fundamentally broken:

```kotlin
// BUGGY original
var rightMin = nums.sliceArray(2 until nums.size).min()  // ← suffix min from index 2

for (i in 1 until nums.lastIndex) {
    val candidateTop = nums[i]
    if (candidateTop < leftMin || candidateTop < rightMin) {
        leftMin = minOf(leftMin, candidateTop)
        rightMin = minOf(rightMin, candidateTop)  // ← wrong!
    }
    minSum = min(minSum, candidateTop + leftMin + rightMin)
}
```

### Bug 1 — `rightMin` is never correctly updated

`rightMin` should be the minimum of `nums[j+1..n-1]` for the *current* `j`. As `j` advances left→right, the right window **shrinks** — elements "fall off" the left edge of that suffix. A running minimum can only shrink, never grow back. You cannot maintain a shrinking-window minimum with a single variable in a forward scan.

```
nums = [1, 10, 2, 3, 4]
j=1 (peak=10): rightMin should be min(2,3,4)=2 ✓  (initialized correctly)
j=2 (peak=2):  rightMin should be min(3,4)=3       but running var still has 2 ✗
j=3 (peak=3):  rightMin should be min(4)=4         but running var still has 2 ✗
```

This causes false mountain validations and wrong sums.

### Bug 2 — Conditional state update corrupts `leftMin`

The `if (candidateTop < leftMin || candidateTop < rightMin)` guard means `leftMin` is only updated when the candidate is smaller than one of the mins. This misses cases where a valid peak appears between two elements that are both larger than the current leftMin — `leftMin` would not advance, leading to stale state.

### Bug 3 — `minSum` initialized to `Int.MIN_VALUE / 2`

This should be `Int.MAX_VALUE`. The function takes the minimum, so initializing to a very negative number means the answer is always that negative number. The sentinel for "no valid triplet found" must be the largest possible value, not the smallest.

### Bug 4 — Wrong return value when no triplet found

The original returns `minSum` which could be `Int.MIN_VALUE / 2` (the initial sentinel) when no valid triplet exists. The problem requires returning `-1` in that case.

---

## Fixed + Commented Code

```kotlin
package two908

import kotlin.math.min

// ─────────────────────────────────────────────────────────────────────────────
// APPROACH 1: Brute Force — O(n²) time, O(n) space
// Strategy: for each candidate peak j, compute leftMin and rightMin by
//           scanning the subarrays. Simple and correct, but slow.
// ─────────────────────────────────────────────────────────────────────────────
class BruteForce {
    fun minimumSum(nums: IntArray): Int {
        // Build index→value map for clean (index, value) iteration.
        val idxToValue = nums.indices.associateWith { nums[it] }

        // Use MAX_VALUE as the "no valid triplet yet" sentinel.
        var minSum = Int.MAX_VALUE

        for ((idx, num) in idxToValue) {
            // Min of everything strictly left of idx.
            // Returns null if idx == 0 (empty left side → not a valid peak).
            val leftSideMin = nums.slice(0 until idx).minOrNull()

            // Min of everything strictly right of idx.
            // Returns null if idx == last index (empty right side → not valid).
            val rightSideMin = nums.slice(idx + 1 until nums.size).minOrNull()

            // Valid mountain: both sides non-null AND peak strictly greater
            // than both valley minimums.
            if (leftSideMin != null && rightSideMin != null &&
                num > leftSideMin && num > rightSideMin
            ) {
                minSum = minOf(minSum, num + leftSideMin + rightSideMin)
            }
        }

        // If sentinel unchanged, no valid triplet was found.
        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// APPROACH 2: Optimized — O(n) time, O(n) space
// Strategy: precompute suffix minimums (rightMin array), then do a single
//           left-to-right pass maintaining a running leftMin.
//
// Key insight: for peak at index j, the best triplet uses:
//   - leftMin  = min(nums[0..j-1])   maintained as running variable
//   - rightMin = min(nums[j+1..n-1]) looked up from precomputed array
// ─────────────────────────────────────────────────────────────────────────────
class Optimized {
    fun minimumSum(nums: IntArray): Int {
        val n = nums.size
        if (n < 3) return -1   // Can't form a triplet with fewer than 3 elements.

        // --- Step 1: Build suffix minimum array ---
        // rightMin[i] answers: "what is the smallest value from index i to end?"
        val rightMin = IntArray(n)
        rightMin[n - 1] = nums[n - 1]            // Base case: last element.
        for (i in n - 2 downTo 0) {              // Fill right→left.
            rightMin[i] = minOf(nums[i], rightMin[i + 1])
        }

        // --- Step 2: Left-to-right pass over candidate peaks ---
        var leftMin = nums[0]    // Will hold min(nums[0..j-1]) at each iteration.
        var minSum = Int.MAX_VALUE

        // j ∈ [1, n-2]: must have at least one element on each side.
        for (j in 1 until n - 1) {
            val peak = nums[j]
            val rightVal = rightMin[j + 1]   // Best right valley for this peak.

            // Mountain condition: peak strictly greater than both valleys.
            if (peak > leftMin && peak > rightVal) {
                minSum = minOf(minSum, peak + leftMin + rightVal)
            }

            // Expand left window AFTER using leftMin, so that during this
            // iteration leftMin correctly represents min(nums[0..j-1]).
            leftMin = minOf(leftMin, peak)
        }

        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }
}
```

---

## Complexity Analysis

| Approach | Time | Space | Notes |
|---|---|---|---|
| Brute Force | O(n²) | O(n) | `slice` allocates new lists; `minOrNull` is O(n) per call |
| Optimized | O(n) | O(n) | One pass for `rightMin`, one pass for the main loop |
| Optimal (possible) | O(n) | O(1) | Can avoid `rightMin` array using a right→left pass first, but O(n) space version is cleaner |

---

## Edge Cases

| Input | Why tricky | Expected output |
|---|---|---|
| `[1, 2]` | Fewer than 3 elements | -1 |
| `[3, 2, 1]` | Strictly decreasing, no valid peak | -1 |
| `[1, 2, 1]` | Exactly one valid triplet | 4 |
| `[1, 2, 3]` | Strictly increasing, no valid peak (3 has no right neighbor) | -1 |
| `[2, 1, 2]` | Middle (1) not greater than neighbors | -1 |
| All equal `[5,5,5]` | Peak not *strictly* greater | -1 |
| Large array with answer at boundaries | `leftMin` or `rightMin` from edges | Must handle correctly |

---

## Key Takeaways

1. **Fix the objective clearly first.** The problem asks for minimum sum of three elements forming a mountain shape. Reading "minimum" and "mountain" together is the whole problem.

2. **Brute force is your specification.** The O(n²) solution is easy to reason about and verify. Write it first; use it to validate the optimized version.

3. **Suffix/prefix precomputation is a power tool.** Whenever you need "best value to my right/left" for every index, precompute it in one pass. This pattern appears in Stock Buy/Sell, Trap Rain Water, Product Except Self, and many more.

4. **Running-minimum-only approaches fail for shrinking windows.** A single running variable can track the minimum of a growing prefix or suffix, but NOT a shrinking one. When the window shrinks (forward scan consuming elements from the left of the suffix), you need the precomputed array.

5. **Update order matters.** `leftMin = minOf(leftMin, peak)` must happen *after* the sum check, or you're including the current peak in its own left window — violating the strict `i < j` requirement.

6. **Sentinel values must match your aggregation direction.** Minimizing? Initialize to `Int.MAX_VALUE`. Maximizing? Initialize to `Int.MIN_VALUE`. Never mix them.
