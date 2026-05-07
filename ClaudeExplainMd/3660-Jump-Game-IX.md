# LeetCode 3660 — Jump Game IX

## Problem

Given `nums: IntArray`, for each index `i` you can jump to any index `j` where:
- `j > i` and `nums[j] < nums[i]` (jump right to a smaller value)
- `j < i` and `nums[j] > nums[i]` (jump left to a larger value)

Jumping is transitive — you can keep jumping. Find for each index the **maximum value reachable** from it.

---

## Core Insight — Why This Is a Connected Components Problem

Two indices `i` and `j` form an **inversion** if the one with the smaller index has the larger value. The jump rules say exactly: *you can jump between two indices iff they form an inversion.*

Because jumping is bidirectional and transitive, this defines an undirected graph where:
- **Nodes** = indices
- **Edges** = inversion pairs

The answer for every node is the **maximum `nums[i]` across its entire connected component**, because once you can reach any node in the component you can reach them all.

---

## Structural Property — Components Have Monotone Max Values

This is the key observation that makes all three O(n) solutions possible.

**Claim:** If you scan left to right and track the current connected components, their maximum values are always non-decreasing.

**Why:** Suppose component A is left of component B, with `aMax > bMax`. Then any element in A with value `aMax` forms an inversion with any element in B with value `bMax < aMax`, meaning A and B could reach each other — contradicting that they're separate components. So `aMax ≤ bMax` always holds.

**Consequence:** When a new element `nums[j]` arrives, it only needs to check its *immediately left neighbour* component. If it can merge with that one it will definitely merge with all components to the left that also have max > `nums[j]`, because those maxes are even larger (monotone). You never need to look further back. This is what makes a monotonic stack sufficient.

---

## Dry Run Setup

All three solutions are traced on the same input:

```
nums = [2, 3, 1]
         0  1  2
```

Expected output: `[3, 3, 3]`

Why? Index 1 (val 3) can jump right to index 2 (val 1 < 3) — edge (1,2).
Index 0 (val 2) can jump right to index 2 (val 1 < 2) — edge (0,2).
All three are in one component, max = 3.

---

## Solution 1 — Monotonic Stack + Union-Find (Claude)

### Intuition

Use a monotonic stack to detect which components should merge (same as Editorial 2 below), but track the merges explicitly with Union-Find so the final answers can be read off in O(α(n)) per query.

The stack stores `(representativeIndex, effectiveMax)` where `effectiveMax` is the maximum value of the entire component that representative stands for — not just `nums[representativeIndex]`.

### Why `effectiveMax` and Not `nums[idx]`?

After merging components, the representative on the stack might have a small `nums[idx]` value even though its component contains a much larger value. When the next element `nums[j]` arrives and checks `stack.last().second > nums[j]`, it needs to compare against the *component's* max, not the representative's raw value. Otherwise a component carrying max=5 but represented by an index with value=2 would be invisible to elements with value=3.

### Algorithm

```
for each j from 0 to n-1:
    curMax = nums[j]
    while stack not empty AND stack.top.effectiveMax > nums[j]:
        pop (idx, effMax)
        union(idx, j)          // merge their components
        curMax = max(curMax, effMax)
    push (j, curMax)           // j represents the merged super-component

answer[i] = compMax[find(i)]   // root's stored max
```

### Code

```kotlin
class Solution {
    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val parent = IntArray(n) { it }
        val compMax = nums.copyOf()

        fun find(x: Int): Int {
            if (parent[x] != x) parent[x] = find(parent[x])
            return parent[x]
        }

        fun union(a: Int, b: Int) {
            val ra = find(a); val rb = find(b)
            if (ra == rb) return
            parent[rb] = ra
            compMax[ra] = maxOf(compMax[ra], compMax[rb])
        }

        val stack = ArrayDeque<Pair<Int, Int>>() // (index, effectiveMax)

        for (j in 0 until n) {
            var curMax = nums[j]
            while (stack.isNotEmpty() && stack.last().second > nums[j]) {
                val (idx, effMax) = stack.removeLast()
                union(idx, j)
                curMax = maxOf(curMax, effMax)
            }
            stack.addLast(j to curMax)
        }

        return IntArray(n) { compMax[find(it)] }
    }
}
```

### Dry Run — `[2, 3, 1]`

```
j=0, nums[j]=2: stack empty → push (0, 2).   Stack: [(0,2)]

j=1, nums[j]=3: stack.top.effMax=2, not > 3 → no merge.
                push (1, 3).                  Stack: [(0,2), (1,3)]

j=2, nums[j]=1:
  pop (1, effMax=3): 3 > 1 → union(1,2), curMax=max(1,3)=3
  pop (0, effMax=2): 2 > 1 → union(0,2), curMax=max(3,2)=3
  stack empty → push (2, 3).               Stack: [(2,3)]

Union-Find after:
  parent: [2, 2, 2] (all point to root 2, or transitively)
  compMax[root] = 3

answer: [compMax[find(0)], compMax[find(1)], compMax[find(2)]]
      = [3, 3, 3] ✓
```

### Complexity
- Time: O(n α(n)) — each element pushed/popped once, union-find ops near-constant
- Space: O(n)

---

## Solution 2 — Monotonic Stack with Interval Tracking (Editorial Approach 2)

### Intuition

The same connected-components idea, but simpler to implement: instead of Union-Find, each stack entry directly owns a range `[left, right]` covering all indices in its component. When components merge, you absorb the popped entry's left boundary and take the max value. At the end, fill in the answer array by walking each surviving stack entry's range.

### Why This Works Without Union-Find

The editorial's key structural observation: **components on the stack are always contiguous index ranges with non-decreasing max values.** When `nums[j]` merges with the top component, it only ever merges with adjacent components, so the resulting merged component is still a contiguous range. You never need to jump around in the array — the `left` pointer absorbs everything naturally.

### Algorithm

```
for each i:
    current = Item(value=nums[i], left=i, right=i)
    while stack not empty AND stack.top.value > nums[i]:
        top = stack.pop()
        current.value = max(current.value, top.value)
        current.left = top.left    // absorb top's entire left extent
    stack.push(current)

for each item in stack:
    answer[item.left..item.right] = item.value
```

### Code

```kotlin
class Editorial2Solution {
    data class Item(var value: Int, var left: Int, var right: Int)

    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)
        val stack = mutableListOf<Item>()

        for (i in nums.indices) {
            val current = Item(value = nums[i], left = i, right = i)
            while (stack.isNotEmpty() && stack.last().value > nums[i]) {
                val top = stack.removeAt(stack.lastIndex)
                current.value = maxOf(current.value, top.value)
                current.left = top.left  // absorb the popped component's range
            }
            stack.add(current)
        }

        for (item in stack) {
            for (index in item.left..item.right) {
                answer[index] = item.value
            }
        }

        return answer
    }
}
```

### Dry Run — `[2, 3, 1]`

```
i=0, nums[i]=2: current=(val=2, left=0, right=0)
  stack empty → push.   Stack: [(2,0,0)]

i=1, nums[i]=3: current=(val=3, left=1, right=1)
  stack.top.value=2, not > 3 → no merge.
  push.                 Stack: [(2,0,0), (3,1,1)]

i=2, nums[i]=1: current=(val=1, left=2, right=2)
  pop (val=3,left=1,right=1): 3>1 → current.value=max(1,3)=3, current.left=1
  pop (val=2,left=0,right=0): 2>1 → current.value=max(3,2)=3, current.left=0
  stack empty → push.   Stack: [(3,0,2)]

Fill answers:
  item=(val=3,left=0,right=2) → answer[0..2] = 3

Result: [3, 3, 3] ✓
```

### Comparison to Solution 1

| | Solution 1 (Union-Find) | Solution 2 (Interval) |
|---|---|---|
| Merge tracking | Union-Find tree | `left` pointer absorption |
| Answer lookup | `compMax[find(i)]` | Range fill at the end |
| Code complexity | Slightly more | Simpler |
| Both correct | Yes | Yes |

Solution 2 is strictly simpler — the contiguous-range property means Union-Find is overkill here. Solution 1 would be the right tool if components could be non-contiguous (e.g. in a general graph).

---

## Solution 3 — Interval Divide and Conquer (Editorial Approach 1)

### Intuition

Instead of building up components from left to right, this approach works **top-down** by exploiting a recursive structure in how the global maximum divides the array.

**Key observation:** The global maximum `rMax1` at index `i1` splits the array into two parts:
- All elements to the **right** of `i1` can jump directly to `rMax1` (they're all smaller). Their answer is `rMax1`.
- Elements to the **left** need further analysis.

For the left part `[0, i1-1]`, find its prefix maximum `rMax2` at index `i2`. Now ask: can any element in `[i2, i1-1]` chain through to the right side and eventually reach `rMax1`?

The bridge between left and right is the **minimum value in the right interval** (`rMin`). If `rMax2 > rMin`, then `rMax2` can jump to `rMin` (since `rMin < rMax2`), and from `rMin` you can reach the entire right component including `rMax1`. So the answer for `[i2, i1-1]` becomes `rMax1`.

If `rMax2 ≤ rMin`, no element in the current left interval can cross over. Their answer is just `rMax2`.

Then recurse on `[0, i2-1]` with updated `rMin` and `rMax`.

### The `rMin` Update Trick

After deciding the answer for `[i2, i1-1]`, the next call needs an updated `rMin`. Whether we bridged or not, we apply:

```
nextRightMin = min(rMin, min(nums[i2..i1-1]))
```

**Why this is safe even when we didn't bridge:** If `rMax2 ≤ rMin` (no bridge), then `rMin ≥ rMax2 ≥` all values in `[i2, i1-1]`. So `min(nums[i2..i1-1])` will always be less than `rMin`, making the old `rMin` irrelevant — the new minimum comes from the current interval, which is correct because the current interval's min is the smallest value that can now be reached from the left.

### Preprocessing

Before recursing, precompute `prevMax[i]` = the prefix maximum up to index `i` and its location. This lets each recursive call instantly find `rMax2` and `i2` for its interval in O(1), making the total work O(n).

### Code

```kotlin
class EditorialSolution {
    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)
        val prevMax = Array(n) { 0 to 0 }

        var currentMax = Int.MIN_VALUE
        var currentMaxIndex = -1
        for (i in nums.indices) {
            if (nums[i] > currentMax) {
                currentMax = nums[i]
                currentMaxIndex = i
            }
            prevMax[i] = currentMax to currentMaxIndex
        }

        fun process(right: Int, rightMin: Int, rightMax: Int) {
            val (prefixMax, pivotIndex) = prevMax[right]

            // Can this interval bridge to the right side?
            val currentAnswer = if (prefixMax <= rightMin) prefixMax else rightMax

            var nextRightMin = minOf(prefixMax, rightMin)
            for (i in pivotIndex..right) {
                answer[i] = currentAnswer
                nextRightMin = minOf(nextRightMin, nums[i])
            }

            if (pivotIndex == 0) return

            process(pivotIndex - 1, nextRightMin, currentAnswer)
        }

        process(right = n - 1, rightMin = Int.MAX_VALUE, rightMax = 0)
        return answer
    }
}
```

### Dry Run — `[2, 3, 1]`

```
Precompute prevMax:
  i=0: max=2 at 0 → prevMax[0]=(2,0)
  i=1: max=3 at 1 → prevMax[1]=(3,1)
  i=2: max=3 at 1 → prevMax[2]=(3,1)

Call process(right=2, rightMin=MAX, rightMax=0):
  prevMax[2] = (prefixMax=3, pivotIndex=1)
  prefixMax=3 > rightMin=MAX? No (3 ≤ MAX) → currentAnswer = prefixMax = 3
  nextRightMin = min(3, MAX) = 3
  Fill answer[1..2] = 3, updating nextRightMin:
    i=1: answer[1]=3, nextRightMin=min(3,3)=3
    i=2: answer[2]=3, nextRightMin=min(3,1)=1
  pivotIndex=1, not 0 → recurse

Call process(right=0, rightMin=1, rightMax=3):
  prevMax[0] = (prefixMax=2, pivotIndex=0)
  prefixMax=2 > rightMin=1? Yes → currentAnswer = rightMax = 3
  Fill answer[0..0] = 3
  pivotIndex=0 → return

Result: [3, 3, 3] ✓
```

### Complexity
- Time: O(n) — prefix max is O(n), each index is visited exactly once across all recursive calls
- Space: O(n) for `prevMax` and recursion stack depth O(number of prefix maxima)

---

## Side-by-Side Summary

| | Solution 1 | Editorial 2 | Editorial 1 |
|---|---|---|---|
| **Approach** | Monotonic stack + Union-Find | Monotonic stack + interval ranges | Interval divide and conquer |
| **Direction** | Left → right | Left → right | Right → left (recursive) |
| **Merge mechanism** | Union-Find roots | Absorb `left` pointer | Recursion with `rightMin`/`rightMax` |
| **When to use** | Non-contiguous graph components | Contiguous components (this problem) | When recursive structure is natural |
| **Time** | O(n α(n)) | O(n) | O(n) |
| **Space** | O(n) | O(n) | O(n) |
| **Simplest?** | No | Yes | Medium |

---

## Decision Guide

```
Is the graph guaranteed to form contiguous segments?
  YES → Editorial 2 (simplest, direct interval tracking)
  NO  → Solution 1 (Union-Find handles arbitrary topology)

Do you prefer recursive thinking?
  YES → Editorial 1 (divide-and-conquer on prefix maxima)
  NO  → Editorial 2 (clean iterative left-to-right)
```

For this specific problem, **Editorial 2 is the sweet spot** — it directly exploits the contiguous-component property with the least machinery. Solution 1 is more general and Editorial 1 is the most algorithmically elegant if you can see the divide-and-conquer structure.

---

## Pitfalls Encountered During Solving

| Pitfall | Lesson |
|---|---|
| Top-down DFS with memo | Cycles invalidate memoization — DFS doesn't work on cyclic graphs |
| Bellman-Ford relaxation | Correct but O(n² × passes) — TLE on large inputs |
| Union-Find without `effectiveMax` | Comparing `nums[representative]` misses true component max |
| Monotonic stack without `effectiveMax` | Popped components lose their max — later elements can't detect inversions with them |
| Two-pass stack with `repeat(2)` | Doesn't propagate updates back correctly in all cycle shapes |
