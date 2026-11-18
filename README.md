# leetcode-core

> LeetCode solutions in Kotlin, written to be understood, not just to pass.
> Clear code backed by in-depth markdown guides on algorithms and data structures.

---

## About

A personal, actively maintained collection of **130+ LeetCode solutions in Kotlin**, plus a library of annotated DSA guides written from first principles.

The guiding rule: **correctness over cleverness, readability over raw optimization.**
Solutions are written the way code should read in a review: well named, clearly structured, and commented where the logic isn't obvious.

> [!NOTE]
> **The C++ track is discontinued.** `cpp-solutions/` keeps its two solutions (146 LRU Cache, 1584 Min Cost to Connect All Points) for reference but gets no new work. Everything new is in Kotlin.

---

## Repository Structure

```
leetcode-core/
├── kotlin-solutions/              # Gradle project (Kotlin 2.1, JDK 21)
│   ├── src/main/kotlin/
│   │   ├── <number>.kt            # One file per LeetCode problem
│   │   └── misc/                  # Reusable DSA utils, practice, Kotlin study notes
│   ├── src/test/kotlin/           # JUnit 5 tests
│   └── build.gradle.kts
├── ClaudeExplainMd/               # In-depth DSA and Kotlin guides
├── cpp-solutions/                 # Archived C++ track (discontinued)
└── README.md
```

### Solutions

Each solution lives in `kotlin-solutions/src/main/kotlin/` and is named after its LeetCode problem number:

```
743.kt      # Network Delay Time (Dijkstra)
1584.kt     # Min Cost to Connect All Points (MST)
2050.kt     # Parallel Courses III (topological sort + DP)
```

To find one, search for the problem number:

```bash
# In an editor: Ctrl+P / Cmd+P, then type the number
# From the shell:
ls kotlin-solutions/src/main/kotlin/743.kt
```

Every file declares its own `package`, so files that all define `Solution`, `TreeNode` or `ListNode` compile side by side in one project. Many files keep several approaches next to each other, from brute force to optimal. For example, `139.kt` (Word Break) has brute force, memoization and tabulation versions.

### misc

`src/main/kotlin/misc/` holds code that isn't tied to one problem:

- **`DsaUtils/`**: reusable implementations of a Fenwick tree, segment tree, Floyd-Warshall, topological sort and palindrome helpers
- **Practice and experiments**: bidirectional BFS with coroutines, LRU cache, unbounded knapsack, subset sum, kth smallest in a sorted matrix, and more
- **Kotlin in Action notes**: the `Kia*.kt` files, including coroutine error handling with its own JUnit test

---

## Guides (`ClaudeExplainMd/`)

Standalone references written alongside the solutions. Most cover the intuition, annotated code, dry runs, complexity, a decision guide and common pitfalls.

#### Graphs and Shortest Paths

| Guide | Covers |
|---|---|
| `dijkstra-bellmanFord-floydWarshall.md` | Dijkstra, Bellman-Ford (with SPFA) and Floyd-Warshall in Kotlin: path reconstruction, decision guide, complexity summary, pitfalls |
| `Dijkstra-ref.md` | Dijkstra in C++ and Kotlin side by side: language mapping, explicit vs. lazy decrease-key, design decisions |
| `Bellman-Ford-FP-Kotlin.md` | Cheapest Flights Within K Stops (LC 787) with Bellman-Ford, rewritten in progressively more functional Kotlin: `fold`, `generateSequence`, `runningFold`, `buildList` |
| `parallel-courses-mastery.md` | Parallel Courses III (LC 2050) with Kahn's algorithm + DP and DFS + memo, and Parallel Courses II (LC 1494): why greedy fails, bitmask DP |

#### Patterns and Techniques

| Guide | Covers |
|---|---|
| `common-leetcode-patterns.md` | Frequency counter, hash map lookup (Two Sum), graph adjacency lists, BFS with a queue, and when to use each |
| `TwoPointersWindow.md` | Two-pointer zip and windowing iteration in Kotlin, worked through a max-profit problem: wrong approaches, the single-pass fix, why outside-in fails |
| `ConstraintsAnalysis.md` | Temporal vs. spatial constraints and ordering vs. distance: a decision matrix and algorithm selection guide |
| `N-Queens.md` | N-Queens backtracking line by line: setup, conflict check, place, recurse, undo |

#### Data Structures

| Guide | Covers |
|---|---|
| `kotlin-fenwick-segment.md` | Fenwick tree (including 2D), segment tree with lazy propagation, a generic monoid segment tree, decision guide |
| `LRU-Cache-146-LeetCode.md` | LRU Cache (LC 146): hash map + doubly linked list with sentinel nodes, dry run, Kotlin and C++ line by line |

#### Problem Deep Dives

| Guide | Covers |
|---|---|
| `3Sum-lc.md` | 3Sum (LC 15): HashSet deduplication vs. explicit duplicate skipping |
| `Jump-Game-Deep-Dive.md` | Jump Game (LC 55): five solutions, from brute-force recursion through memoization and tabulation to forward and reverse greedy |
| `3660-Jump-Game-IX.md` | Jump Game IX (LC 3660) as a connected-components problem: monotonic stack + union-find, interval tracking, divide and conquer |
| `Kth-Smallest-Element-in-a-Sorted-Matrix.md` | LC 378: binary search on the value range with staircase counting, min-heap k-way merge, midpoint pitfalls |
| `Design-Twitter-LeetCode.md` | Design Twitter (LC 355): three designs, from a `Triple` heap to a `HeapEntry` data class to a flat sort |
| `Lc2908-min-mountain-triplet.md` | Minimum Sum of Mountain Triplets I (LC 2908): brute force vs. an O(n) solution |
| `repeated-substr-pattern.md` | Repeated Substring Pattern (LC 459): why the `s + s` rotation trick works |
| `set-mismatch-guide.md` | Set Mismatch (LC 645): sign marking vs. cyclic sort |
| `2073-time-needed-to-buy-tickets.md` | Time Needed to Buy Tickets (LC 2073): queue simulation vs. an O(n) formula, Kotlin pitfalls |

#### Kotlin for LeetCode

| Guide | Covers |
|---|---|
| `kt-features-leetcode.md`, `kt-leetcode-comprehensive-features.md` | Kotlin stdlib for LeetCode: aggregation, map, collection, transformation and string functions, with worked examples |
| `Kt-labels-deep-dive.md` | Labels: returning from nested lambdas, the `@outer` pattern, break and continue in nested loops, scoping rules |
| `kotlin-competitive-Programming-io.md` | Input reading compared: `Scanner`, `readLine` + `split`, `BufferedReader` + `StringTokenizer`, and byte-level `BufferedInputStream` |
| `kotlin-fastest-io.md` | A ready-to-use fast I/O template: reader cheat sheet, internal data flow, pitfalls |

#### Concurrency

| Guide | Covers |
|---|---|
| `jvm-kotlin-concurrency-ref.md` | Atomics (`AtomicInteger` vs. `LongAdder`), `synchronized` and `ReentrantLock`, concurrent collections, blocking queues, coroutines, patterns, decision guide |
| `Bidirectional-BFS.md` | Bidirectional BFS with coroutines: push model (`launch` + `Deferred`) vs. pull model (`async` + `select`), coroutine exception pitfalls, JUnit tests |

---

## Problem Categories

| Category | Examples |
|---|---|
| Arrays & Two Pointers | 15 3Sum, 18 4Sum, 75 Sort Colors, 283 Move Zeroes, 334 Increasing Triplet Subsequence |
| Strings | 5 Longest Palindromic Substring, 14 Longest Common Prefix, 459 Repeated Substring Pattern |
| Linked Lists | 2 Add Two Numbers, 23 Merge k Sorted Lists, 141 Linked List Cycle, 206 Reverse Linked List |
| Stacks & Monotonic Stack | 84 Largest Rectangle in Histogram, 150 Evaluate Reverse Polish Notation, 739 Daily Temperatures, 853 Car Fleet |
| Trees | 94 / 144 / 145 traversals, 101 Symmetric Tree, 110 Balanced Binary Tree, 226 Invert Binary Tree |
| Graphs: BFS / DFS | 133 Clone Graph, 200 Number of Islands, 797 All Paths From Source to Target, 1091 Shortest Path in Binary Matrix |
| Graphs: Topological Sort | 207 Course Schedule, 210 Course Schedule II, 802 Find Eventual Safe States, 2050 Parallel Courses III |
| Graphs: Shortest Paths & MST | 743 Network Delay Time, 787 Cheapest Flights Within K Stops, 1334 Find the City, 1514 Path with Maximum Probability, 1584 Min Cost to Connect All Points |
| Union-Find | 547 Number of Provinces, 684 Redundant Connection |
| Dynamic Programming | 70 Climbing Stairs, 139 Word Break, 198 / 213 House Robber, 494 Target Sum, 1049 Last Stone Weight II, 1143 Longest Common Subsequence |
| Bitmask DP | 1494 Parallel Courses II |
| Greedy | 45 Jump Game II, 55 Jump Game, 121 Best Time to Buy and Sell Stock, 1029 Two City Scheduling |
| Heap / Priority Queue | 215 Kth Largest Element, 347 Top K Frequent Elements, 373 Find K Pairs with Smallest Sums, 1834 Single-Threaded CPU |
| Binary Search | 35 Search Insert Position, 704 Binary Search, 378 Kth Smallest Element in a Sorted Matrix |
| Design | 146 LRU Cache, 208 Implement Trie, 307 Range Sum Query - Mutable, 355 Design Twitter, 1352 Product of the Last K Numbers |
| Backtracking | 51 N-Queens, 77 Combinations, 78 Subsets |
| Bit Manipulation | 268 Missing Number, 389 Find the Difference |

---

## Design Standards

**Naming:** expressive over terse, e.g. `settled` instead of `vis`, `travelTime` instead of `w`.

**Style:**
- Idiomatic Kotlin stdlib (`withIndex`, `groupBy`, `fold`, `buildList`, destructuring, and so on)
- Named types over raw primitives where they add clarity, e.g. `data class Edge(val target: Int, val travelTime: Int)` in `743.kt`
- Numbered comments marking the phases of non-trivial algorithms
- No micro-optimizations that hide intent

**Performance:** every solution targets the asymptotically correct complexity class.
Constant-factor tuning happens only when the straightforward approach would exceed the time limit (TLE) on the given constraints.

---

## Running Solutions

`kotlin-solutions/` is a Gradle project that needs **JDK 21**. The Gradle wrapper downloads Gradle itself.

```bash
cd kotlin-solutions
./gradlew build    # compile every solution
./gradlew test     # run the JUnit 5 tests
```

- **IntelliJ IDEA:** open `kotlin-solutions/` as a Gradle project. Files that have a `main()` function run from the gutter icon next to it.
- **LeetCode:** copy the `Solution` class (without the `package` line) into the problem's Kotlin editor.

---

> *"Code should first be understood, then optimized."*
