# LeetCode 378 — Kth Smallest Element in a Sorted Matrix

---

## Problem

Given an `n × n` matrix where:
- Each **row** is sorted ascending
- Each **column** is sorted ascending

Return the **k-th smallest** element in the matrix.

> The answer must be an **actual element** in the matrix — not just a rank.

```
matrix = [            k = 8
  [ 1,  5,  9],
  [10, 11, 13],       answer = 13
  [12, 13, 15]
]
```

---

## Core Insight — What Makes This Problem Special

The matrix is **doubly sorted**: every row and column is in ascending order.  
This means the global minimum is always at `matrix[0][0]` and the global maximum at `matrix[n-1][n-1]`.

This structural property enables two elegant approaches:

| Approach | When | Time | Space |
|---|---|---|---|
| **Binary Search on value range** | General case | `O(n · log(max - min))` | `O(1)` |
| **Min Heap (k-way merge)** | Small `k` | `O(k · log n)` | `O(n)` |

---

## Approach 1 — Binary Search on Value Range

### Intuition

Instead of searching for a **position** (like classic binary search), we binary search over the **range of values** `[matrix[0][0], matrix[n-1][n-1]]`.

For any candidate `mid`, we ask:

> *How many elements in the matrix are ≤ mid?*

- If `count < k` → the k-th smallest is **larger** → `left = mid + 1`
- If `count ≥ k` → the k-th smallest is **mid or smaller** → `right = mid`

The loop converges when `left == right`, which is guaranteed to be an **actual element** in the matrix.

### Why Does `left` Always Land on a Real Element?

Because we only move `left` to `mid + 1` when `count < k`.  
`left` will stop at the smallest value `v` such that `countLessOrEqual(v) >= k`.  
That value must exist in the matrix — otherwise decreasing it by 1 would give a `count < k`.

### Staircase Counting — `countLessOrEqual` in O(n)

This is the heart of the algorithm.

**Start at the bottom-left corner** `(n-1, 0)`:

```
     col→
      0    1    2
row 0 [ 1,  5,  9]
row 1 [10, 11, 13]
row 2 [12, 13, 15]   ← start here
      ↑
```

At each step:
- If `matrix[row][col] <= target` → every cell **above** in this column is also `<= target`
  → add `row + 1` to count, move **right** (`col++`)
- Else → this cell is too large → move **up** (`row--`)

Each step eliminates either a whole column (from counting above) or a row from consideration — so we make at most `2n` moves = `O(n)`.

**Visual trace** for `target = 13`:

```
Step 1: (2,0) → matrix[2][0] = 12 ≤ 13 → count += 3 → col = 1
Step 2: (2,1) → matrix[2][1] = 13 ≤ 13 → count += 3 → col = 2
Step 3: (2,2) → matrix[2][2] = 15 > 13 → row = 1
Step 4: (1,2) → matrix[1][2] = 13 ≤ 13 → count += 2 → col = 3
col = 3 = n → stop

Total count = 3 + 3 + 2 = 8 ✓
```

### Code

```kotlin
class SolutionBinarySearch {

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {

        var left = matrix[0][0]
        var right = matrix.last().last()

        while (left < right) {

            val mid = left + (right - left) / 2   // overflow-safe midpoint

            val count = countLessOrEqual(matrix, mid)

            if (count < k) {
                left = mid + 1
            } else {
                right = mid
            }
        }

        return left
    }

    private fun countLessOrEqual(matrix: Array<IntArray>, target: Int): Int {

        val n = matrix.size
        var row = n - 1
        var col = 0
        var count = 0

        while (row >= 0 && col < n) {

            if (matrix[row][col] <= target) {
                count += row + 1   // everything above is also <= target
                col++
            } else {
                row--
            }
        }
        return count
    }
}
```

### Complexity

| | |
|---|---|
| **Time** | `O(n · log(max - min))` — `log(valueRange)` binary search steps × `O(n)` counting each step |
| **Space** | `O(1)` — no auxiliary data structures |

---

## Approach 2 — Min Heap (k-Way Merge)

### Intuition

Treat each **row** as an independent sorted stream.  
Merge all `n` streams simultaneously using a min-heap.  
Extract the global minimum `k` times — the k-th extraction is the answer.

```
Row 0:  1 →  5 →  9
Row 1: 10 → 11 → 13
Row 2: 12 → 13 → 15

Heap initially: [1, 10, 12]

Extract 1 (count=1), push 5   → heap: [5, 10, 12]
Extract 5 (count=2), push 9   → heap: [9, 10, 12]
Extract 9 (count=3), push -   → heap: [10, 12]
...
Extract the k-th minimum
```

### Algorithm Steps

1. **Initialize heap** with `(matrix[row][0], row, 0)` for all rows.
2. **Pop k-1 times** — each pop removes the current global minimum, and inserts the next element from the same row.
3. **Peek** at the heap after k-1 pops → that is the k-th smallest.

### Code

```kotlin
class SolutionHeap {

    data class Element(val value: Int, val row: Int, val col: Int)

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        val n = matrix.size

        val minHeap = PriorityQueue<Element>(compareBy { it.value })

        // seed with the first element of every row
        for (row in 0 until n) {
            minHeap.offer(Element(matrix[row][0], row, 0))
        }

        // pop k-1 times, push next from same row
        repeat(k - 1) {
            val current = minHeap.poll() ?: return@repeat
            val nextCol = current.col + 1
            if (nextCol < n) {
                minHeap.offer(Element(matrix[current.row][nextCol], current.row, nextCol))
            }
        }

        return minHeap.peek().value
    }
}
```

### Complexity

| | |
|---|---|
| **Time** | `O(k · log n)` — `k` heap operations, heap size always ≤ `n` |
| **Space** | `O(n)` — at most one element per row in the heap |

---

## Approach 3 — "Strictly Less" Binary Search Variant

### Intuition

A less common but valid framing: instead of counting `<= mid`, count elements **strictly less than mid**.

We define `target = k - 1` (we want exactly `k-1` elements strictly smaller than our answer).

Key difference: the binary search uses a **right-biased midpoint** and closes toward `left`:

```kotlin
val mid = right - (right - left) / 2   // right-biased
```

Why right-biased? When `left` and `right` are neighbors, a left-biased midpoint `(left + right) / 2 = left` causes `left = mid` → infinite loop. The right-biased version picks `right`, guarantees progress.

### Code

```kotlin
class PracticeSolution {

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix.first().first()
        var right = matrix.last().last()
        val targetLessOrEqual = k - 1

        while (left < right) {
            val mid = right - (right - left) / 2   // right-biased — prevents infinite loop

            if (countStrictlyLess(matrix, mid) <= targetLessOrEqual) {
                left = mid   // mid could be the answer
            } else {
                right = mid - 1
            }
        }
        return left
    }

    private fun countStrictlyLess(matrix: Array<IntArray>, target: Int): Int {
        var count = 0
        var row = matrix.size - 1
        var col = 0
        while (row >= 0 && col < matrix[0].size) {
            if (matrix[row][col] < target) {   // strictly less
                count += (row + 1)
                col++
            } else {
                row--
            }
        }
        return count
    }
}
```

### Why Both Variants Are Correct

| Variant | Count | Condition | Move |
|---|---|---|---|
| `<= mid` | how many ≤ mid | `count < k` → `left = mid + 1` | close toward smallest valid value |
| `< mid` | how many < mid | `count <= k-1` → `left = mid` | close toward smallest value with enough "room below" |

Both terminate at the same answer — the smallest value where exactly `k` elements are ≤ it.

---

## Midpoint Pitfalls in Binary Search

### Overflow-Safe Left-Biased (Standard)

```kotlin
val mid = left + (right - left) / 2
```

Use when: `right = mid` on the `≥ k` branch (closing from above).

### Right-Biased

```kotlin
val mid = right - (right - left) / 2
```

Use when: `left = mid` on the `>= condition` branch.  
Without right-bias, `left = mid = left` loops forever when they're neighbors.

### Rule of Thumb

> If you write `left = mid` anywhere in your binary search, use a **right-biased** midpoint.

---

## Staircase Counting — Deep Dive

The `O(n)` counting trick is reusable across many "sorted matrix" problems.

```
Matrix:
  [ 1,  5,  9]
  [10, 11, 13]
  [12, 13, 15]

target = 11

Start: row=2, col=0

(2,0): 12 > 11 → row-- → row=1
(1,0): 10 ≤ 11 → count += 2 (rows 0,1 at col 0) → col=1
(1,1): 11 ≤ 11 → count += 2 (rows 0,1 at col 1) → col=2
(1,2): 13 > 11 → row-- → row=0
(0,2):  9 ≤ 11 → count += 1 (row 0 at col 2) → col=3
col=3=n → stop

Total = 2 + 2 + 1 = 5
```

The 5 elements ≤ 11 are: `1, 5, 9, 10, 11` ✓

---

## Approach Comparison

```
                    ┌─────────────────────────────────────┐
                    │        n x n Sorted Matrix          │
                    └─────────────┬───────────────────────┘
                                  │
                   ┌──────────────▼──────────────┐
                   │  Is k small relative to n?  │
                   └──────────────┬──────────────┘
                                  │
              ┌───────────────────┴───────────────────┐
              │ YES (k ~ log n or smaller)             │ NO (k ~ n²)
              ▼                                        ▼
    ┌─────────────────────┐              ┌──────────────────────────┐
    │  Min Heap           │              │  Binary Search on values │
    │  O(k log n)         │              │  O(n log(max-min))       │
    │  O(n) space         │              │  O(1) space              │
    └─────────────────────┘              └──────────────────────────┘
```

**In practice:** Binary Search is almost always preferred — its `O(1)` space and predictable `O(n log V)` make it the go-to in interviews.

---

## Kotlin Coercion Helpers (Bonus)

Kotlin has clean built-ins for clamping values — useful in matrix/grid problems:

```kotlin
val a = 10
a.coerceAtMost(5)     // min(a, 5) → 5

val b = 5
b.coerceAtLeast(10)   // max(b, 10) → 10

val c = 3
c.coerceIn(1..5)      // clamp(c, 1, 5) → 3
```

| Function | Equivalent | Use |
|---|---|---|
| `coerceAtMost(max)` | `min(value, max)` | cap an upper bound |
| `coerceAtLeast(min)` | `max(value, min)` | enforce a lower bound |
| `coerceIn(range)` | `clamp(value, lo, hi)` | restrict to a range |

---

## Common Pitfalls

| Pitfall | Fix |
|---|---|
| Binary search on index instead of value | Search over `[matrix[0][0], matrix[n-1][n-1]]` — the value range |
| Assuming `mid` is always in the matrix | It's not, but the final `left` is guaranteed to be |
| Infinite loop with `left = mid` and left-biased midpoint | Switch to right-biased `mid = right - (right - left) / 2` |
| Off-by-one in staircase counting | Count is `row + 1`, not `row` (0-indexed rows) |
| Counting `<` instead of `<=` in standard variant | Match your count semantics to your convergence condition |
| Heap approach: forgetting bounds check on `nextCol < n` | Always guard before offering to heap |

---

## Decision Guide

```
Need O(1) space?                          → Binary Search
k is tiny (k << n²)?                      → Min Heap (faster in practice for small k)
Rows are much longer than matrix is tall? → Min Heap (only n initial inserts regardless of row length)
General / interview default?              → Binary Search on value range
```

---

## Related Problems

| Problem | Key Idea |
|---|---|
| **LeetCode 240** — Search in 2D Matrix II | Same staircase O(n) walk |
| **LeetCode 373** — Find K Pairs with Smallest Sums | k-way heap merge |
| **LeetCode 668** — Kth Smallest Number in Multiplication Table | Binary search + counting |
| **LeetCode 719** — Find K-th Smallest Pair Distance | Binary search on value range |
