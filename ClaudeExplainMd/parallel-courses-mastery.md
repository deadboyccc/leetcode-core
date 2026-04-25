# Parallel Courses — Deep Study Guide

> LC 1494 · Parallel Courses II  
> LC 2050 · Parallel Courses III

---

## Table of Contents

1. [Problem Comparison — Know What You're Solving](#1-problem-comparison)
2. [The Foundation — Topological Sort (Kahn's Algorithm)](#2-the-foundation--topological-sort)
3. [LC 2050 — Parallel Courses III](#3-lc-2050--parallel-courses-iii)
    - [Intuition](#31-intuition)
    - [Solution A — Kahn's + DP (Bottom-Up)](#32-solution-a--kahns--dp-bottom-up)
    - [Solution B — DFS + Memo (Top-Down)](#33-solution-b--dfs--memo-top-down)
4. [LC 1494 — Parallel Courses II](#4-lc-1494--parallel-courses-ii)
    - [Why Greedy Fails](#41-why-greedy-fails)
    - [The Key Insight — Bitmask DP](#42-the-key-insight--bitmask-dp)
    - [Solution A — Bitmask DP (Bottom-Up)](#43-solution-a--bitmask-dp-bottom-up)
    - [Solution B — Bitmask DFS + Memo (Top-Down)](#44-solution-b--bitmask-dfs--memo-top-down)
5. [Techniques Reference](#5-techniques-reference)

---

## 1. Problem Comparison

Before writing a single line of code, understand **exactly** how the two problems differ.

|                | LC 2050 (III)                           | LC 1494 (II)                   |
|----------------|-----------------------------------------|--------------------------------|
| Parallel limit | **Unlimited** — take any number at once | **k courses max** per semester |
| Course weight  | Each has a `time[i]` duration           | All equal — 1 semester each    |
| Goal           | Min total **months**                    | Min total **semesters**        |
| n constraint   | Up to **50,000**                        | Up to **15**                   |
| Algorithm      | Topo sort + DP                          | Bitmask DP / DFS               |

The `n ≤ 15` constraint on LC 1494 is the loudest hint in the problem.  
It screams: **bitmask over the power set**. More on why shortly.

---

## 2. The Foundation — Topological Sort

Both problems are built on a DAG (directed acyclic graph) of prerequisites.  
Topological sort gives you a guaranteed processing order: **prerequisites always come before dependents**.

### Kahn's Algorithm (BFS-based topo sort)

```
1. Compute inDegree[node] for every node
2. Seed a queue with all nodes where inDegree == 0 (no prerequisites)
3. While queue is not empty:
     node = queue.pop()
     for each neighbor of node:
         inDegree[neighbor]--
         if inDegree[neighbor] == 0:
             queue.add(neighbor)   ← all prerequisites are now done
```

The moment `inDegree[neighbor]` hits 0, you know **every prerequisite** of that neighbor has been processed. This is the
property both problems exploit.

---

## 3. LC 2050 — Parallel Courses III

### 3.1 Intuition

You can take **any number of courses in parallel**. So the question becomes:  
*"What is the longest chain of work I must do sequentially?"*

Think of it like a factory pipeline. You have workers (unlimited), and each task takes some days. The total time is not
the sum of all tasks — it's the **length of the critical path**: the longest sequence of dependent tasks end-to-end.

**Concrete example:**

```
Course 1 (3 months) ──┐
                       ├──► Course 3 (5 months)
Course 2 (2 months) ──┘

Answer: 3 + 5 = 8 months
```

Course 1 and 2 run in parallel. Course 3 must wait for **both** — it waits for the **slowest** one (course 1, 3 months),
then adds its own 5. Course 2's 2 months are hidden behind course 1.

The key recurrence:

```
dp[course] = max(dp[prereq] for all prereqs) + time[course]
```

### 3.2 Solution A — Kahn's + DP (Bottom-Up)

Process nodes in topological order. When we finish processing a node, we propagate its completion time forward to its
neighbors.

```kotlin
fun minimumTime(n: Int, relations: Array<IntArray>, time: IntArray): Int {
    val graph = Array(n + 1) { mutableListOf<Int>() }
    val inDegrees = IntArray(n + 1) { 0 }
    relations.forEach { (prev, next) ->
        graph[prev].add(next)
        inDegrees[next]++
    }

    // dp[i] = earliest month course i can finish
    val dp = IntArray(n + 1) { 0 }

    val queue = ArrayDeque<Int>()
    (1..n).forEach { course ->
        if (inDegrees[course] == 0) {
            dp[course] = time[course - 1]   // no prereqs → starts at month 0
            queue.addLast(course)
        }
    }

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        graph[node].forEach { neighbor ->
            // neighbor must wait for node to finish, then adds its own duration
            dp[neighbor] = maxOf(dp[neighbor], dp[node] + time[neighbor - 1])

            // enqueue only when ALL prerequisites are processed
            if (--inDegrees[neighbor] == 0) queue.addLast(neighbor)
        }
    }

    return dp.max()
}
```

**Why `maxOf` and not `+`?**  
Because multiple prerequisites feed into one course. Each predecessor independently tries to push `dp[neighbor]` higher.
The `maxOf` accumulates the **worst-case arrival time** across all predecessors. When `inDegrees[neighbor]` finally hits
0, we've seen all predecessors — so `dp[neighbor]` is final.

**Complexity:** O(N + E) time, O(N) space. Handles n = 50,000 easily.

---

### 3.3 Solution B — DFS + Memo (Top-Down)

Instead of pushing forward from prerequisites, pull backward from each course:  
*"What is the minimum time to complete course C, given all its prerequisites?"*

Build a **reverse graph**: edges point from a course back to its prerequisites.

```kotlin
fun minimumTime(n: Int, relations: Array<IntArray>, time: IntArray): Int {
    // reverse graph: prereqs[c] = list of courses that must finish before c
    val prereqs = Array(n + 1) { mutableListOf<Int>() }
    relations.forEach { (prev, next) -> prereqs[next].add(prev) }

    val memo = IntArray(n + 1) { -1 }

    // returns the earliest month course c can finish
    fun dfs(c: Int): Int {
        if (memo[c] != -1) return memo[c]

        // if no prerequisites, starts immediately and finishes after time[c-1]
        // otherwise, must wait for the slowest prerequisite
        val result = prereqs[c].maxOfOrNull { dfs(it) + time[c - 1] } ?: time[c - 1]

        memo[c] = result
        return result
    }

    // answer is the course that finishes latest overall
    return (1..n).maxOf { dfs(it) }
}
```

**Why does `dfs(prereq) + time[c-1]` work?**  
`dfs(prereq)` gives the month that prerequisite finishes. That's the earliest `c` can **start**. Add `time[c-1]` to get
when `c` finishes. Take `max` over all prerequisites to find the true earliest start.

**Bottom-up vs Top-down — which to prefer?**

|                        | Kahn's DP    | DFS + Memo           |
|------------------------|--------------|----------------------|
| Direction              | Push forward | Pull backward        |
| Stack overflow risk    | No           | Yes for deep graphs  |
| Easier to reason about | Graph flow   | Recursive dependency |

For LC 2050 with n up to 50,000, prefer Kahn's DP. DFS risks stack overflow on deep chains.

---

## 4. LC 1494 — Parallel Courses II

### 4.1 Why Greedy Fails

Your original Kahn's solution failed here. The intuition felt right — take up to `k` available courses each semester.
But **which** `k` you pick from the available pool changes what unlocks next.

**Counterexample:**

```
Available this semester: {A, B, C},  k = 2
Prerequisites: course X needs both A and B before it can be taken

Greedy picks {A, C}:
  Sem 1: A, C
  Sem 2: B          ← X still locked, needs A+B done
  Sem 3: X
  Total: 3 semesters

Optimal picks {A, B}:
  Sem 1: A, B
  Sem 2: C, X       ← X now available since A+B done
  Total: 2 semesters
```

Greedy can't see that A and B together are more valuable than A and C together.  
You must **try all valid combinations** of size ≤ k. That's exponential in the number of available courses — and
`n ≤ 15` is the problem's acknowledgment of this.

### 4.2 The Key Insight — Bitmask DP

With `n ≤ 15`, there are at most `2^15 = 32,768` possible subsets of courses.  
Each subset represents a distinct "state": the set of courses already completed.

**State definition:**

```
dp[mask] = minimum semesters to complete exactly the courses encoded in mask
```

A bitmask is just an integer where **bit i = 1 means course i+1 is done**.

```
n = 4 courses
mask = 0b0110 = 6  →  courses 2 and 3 are completed
```

**Prerequisite check using bitmasks:**

```
prereq[course] = bitmask of all prerequisites of that course

Course is available when:
  1. Its bit is NOT set in mask (not yet done)
  2. (prereq[course] & mask) == prereq[course]
     ↑ all prerequisite bits are present in mask
```

**Subset enumeration trick:**

To iterate all non-empty subsets of a bitmask `available`:

```kotlin
var subset = available
while (subset > 0) {
    // process subset
    subset = (subset - 1) and available   // ← standard bit trick
}
```

`(subset - 1) and available` strips the lowest set bit of `subset` while staying within `available`. This visits every
non-empty subset exactly once in O(3^n) total — because each bit is either in `available` but not `subset`, in both, or
in neither (three choices per bit).

### 4.3 Solution A — Bitmask DP (Bottom-Up)

```kotlin
fun minNumberOfSemesters(n: Int, relations: Array<IntArray>, k: Int): Int {
    // encode prerequisites as bitmasks (0-indexed courses internally)
    val prereq = IntArray(n) { 0 }
    relations.forEach { (prev, next) ->
        prereq[next - 1] = prereq[next - 1] or (1 shl (prev - 1))
    }

    // dp[mask] = min semesters to complete all courses in mask
    val dp = IntArray(1 shl n) { Int.MAX_VALUE }
    dp[0] = 0   // base case: 0 courses done in 0 semesters

    for (mask in 0 until (1 shl n)) {
        if (dp[mask] == Int.MAX_VALUE) continue   // unreachable state, skip

        // find every course available given current completed set
        var available = 0
        for (course in 0 until n) {
            val notDone = (mask shr course) and 1 == 0
            val prereqsMet = (prereq[course] and mask) == prereq[course]
            if (notDone && prereqsMet) available = available or (1 shl course)
        }

        // try every valid semester schedule (subset of available, size ≤ k)
        var subset = available
        while (subset > 0) {
            if (subset.countOneBits() <= k) {
                dp[mask or subset] = minOf(dp[mask or subset], dp[mask] + 1)
            }
            subset = (subset - 1) and available
        }
    }

    // all n courses done = all n bits set
    return dp[(1 shl n) - 1]
}
```

**Walk through Example 1:** `n=4, relations=[[2,1],[3,1],[1,4]], k=2`

```
prereq[0] (course 1) = 0b0110  ← needs courses 2 and 3
prereq[1] (course 2) = 0b0000  ← no prereqs
prereq[2] (course 3) = 0b0000  ← no prereqs
prereq[3] (course 4) = 0b0001  ← needs course 1

mask=0000: available={2,3}, best subset of size≤2 = {2,3}
  → dp[0110] = 1

mask=0110: available={1}, only subset = {1}
  → dp[0111] = 2

mask=0111: available={4}
  → dp[1111] = 3

Answer: dp[1111] = 3 ✓
```

**Complexity:** O(3^n) for subset enumeration × O(n) for available computation = O(n · 3^n).  
For n=15: ~14 million operations. Fast enough.

### 4.4 Solution B — Bitmask DFS + Memo (Top-Down)

Same state space, explored recursively. Start from `done=0`, recurse toward `done=fullMask`.

```kotlin
fun minNumberOfSemesters(n: Int, relations: Array<IntArray>, k: Int): Int {
    val prereq = IntArray(n) { 0 }
    relations.forEach { (prev, next) ->
        prereq[next - 1] = prereq[next - 1] or (1 shl (prev - 1))
    }

    val fullMask = (1 shl n) - 1
    val memo = IntArray(1 shl n) { -1 }

    // returns min semesters to finish all remaining courses given `done` completed
    fun dfs(done: Int): Int {
        if (done == fullMask) return 0          // all done
        if (memo[done] != -1) return memo[done] // already solved

        var available = 0
        for (course in 0 until n) {
            val notDone = (done shr course) and 1 == 0
            val prereqsMet = (prereq[course] and done) == prereq[course]
            if (notDone && prereqsMet) available = available or (1 shl course)
        }

        var best = Int.MAX_VALUE
        var subset = available
        while (subset > 0) {
            if (subset.countOneBits() <= k) {
                val sub = dfs(done or subset)
                if (sub != Int.MAX_VALUE)
                    best = minOf(best, 1 + sub)
            }
            subset = (subset - 1) and available
        }

        memo[done] = best
        return best
    }

    return dfs(0)
}
```

**Bottom-up vs Top-down for LC 1494:**

|                 | Bitmask DP                  | Bitmask DFS           |
|-----------------|-----------------------------|-----------------------|
| Visits states   | All 2^n states in order     | Only reachable states |
| Overhead        | Iterates unreachable states | Recursion stack       |
| Easier to debug | Yes — linear scan           | No — harder to trace  |
| Preferred       | Slightly, for this problem  | Fine for n ≤ 15       |

---

## 5. Techniques Reference

### When to use each technique

| Signal in problem                                         | Technique                              |
|-----------------------------------------------------------|----------------------------------------|
| DAG + find order/time                                     | Topological sort (Kahn's)              |
| DAG + longest path + node weights                         | Kahn's DP or DFS+memo on reverse graph |
| n ≤ 15–20 + subset/combination matters                    | Bitmask DP                             |
| "minimum steps to reach a goal state" + small state space | BFS or DP over states                  |

### Bitmask operations cheat sheet

```kotlin
// set bit i
mask = mask or (1 shl i)

// check if bit i is set
(mask shr i) and 1 == 1

// check if all bits of `required` are set in `mask`
(required and mask) == required

// iterate all non-empty subsets of `available`
var subset = available
while (subset > 0) {
    // use subset
    subset = (subset - 1) and available
}

// count bits set (Kotlin built-in)
mask.countOneBits()

// all n bits set (= completed all n courses)
val fullMask = (1 shl n) - 1
```

### The three problems as a progression

```
Parallel Courses I  (LC 1136)
  → Kahn's BFS, count levels. Pure topo sort. No weights, no limit.

Parallel Courses III (LC 2050)
  → Kahn's BFS + DP. Add weights (time[i]). Propagate max finish time.
    Still no limit → greedy works fine.

Parallel Courses II (LC 1494)
  → Add a k-limit → greedy breaks → must enumerate subsets → Bitmask DP.
    n ≤ 15 makes 2^n tractable.
```

Each problem adds one constraint that breaks the previous solution, forcing a more powerful technique. Recognizing that
progression is the real skill.