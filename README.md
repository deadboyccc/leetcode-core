# leetcode-core

> LeetCode solutions in Kotlin, written to be understood, not just to pass.
> Clear code backed by in-depth markdown guides on algorithms and data structures.

---

## About

A personal, actively maintained collection of LeetCode solutions in **Kotlin**, plus a growing library of annotated DSA reference guides written from first principles.

The guiding rule: **correctness over cleverness, readability over raw optimization.**
Solutions are written the way code should read in a review: well named, clearly structured, and commented where the logic isn't obvious.

> [!NOTE]
> **The C++ track is discontinued.** `cpp-solutions/` stays in the repo for reference but gets no new solutions or updates. All new work is in Kotlin.

---

## Repository Structure

```
leetcode-core/
├── kotlin-solutions/     # Main solution track: idiomatic Kotlin
├── cpp-solutions/        # Archived C++ track (discontinued)
├── ClaudeExplainMd/      # In-depth DSA reference guides
└── README.md
```

### kotlin-solutions

Each file is named after its LeetCode problem number:

```
1.kt        # Two Sum
42.kt       # Trapping Rain Water
1114.kt     # Print in Order (concurrency)
```

To find a solution, search for the problem number:

```bash
# In an editor: Ctrl+P / Cmd+P, then type the number
# From the shell:
ls kotlin-solutions/743.kt
```

### ClaudeExplainMd

Standalone references written alongside the solutions. Each guide covers the intuition, an annotated implementation, how it works internally, LeetCode problem walkthroughs, a decision guide, a complexity summary, and common pitfalls.

#### Graphs and Shortest Paths

| Guide | Covers |
|---|---|
| `dijkstra-bellmanFord-floydWarshall.md` | Dijkstra, Bellman-Ford, SPFA, and Floyd-Warshall: annotated implementations, path reconstruction, LeetCode walkthroughs, decision guide |
| `Dijkstra-ref.md` | Dijkstra in depth: heap variants, lazy deletion, `State`/`Edge` types, single-source to all-pairs |
| `Bellman-Ford-FP-Kotlin.md` | Bellman-Ford in functional Kotlin: fold-based relaxation, negative-cycle detection |
| `Bidirectional-BFS.md` | Bidirectional BFS: meet-in-the-middle search, the word-ladder pattern, reduced search space |

#### Patterns and Techniques

| Guide | Covers |
|---|---|
| `common-leetcode-patterns.md` | Catalog of recurring patterns: frequency counter, prefix sum, monotonic stack, intervals |
| `TwoPointersWindow.md` | Two pointers and sliding window: fixed vs. variable windows, same-direction vs. opposite-direction pointers |
| `N-Queens.md` | N-Queens backtracking: constraint propagation, bitmask optimization, counting solutions |
| `ConstraintsAnalysis.md` | Reading constraints to infer the required complexity (e.g. n ≤ 10^6 needs O(n log n) or better) |

#### Data Structures

| Guide | Covers |
|---|---|
| `kotlin-fenwick-segment.md` | Fenwick tree (1D and 2D), segment tree, lazy propagation, generic monoid segment tree |

#### Kotlin for LeetCode

| Guide | Covers |
|---|---|
| `kt-features-leetcode.md` | Kotlin stdlib features for LeetCode: `withIndex`, `groupBy`, `fold`, `buildList`, destructuring |
| `kt-leetcode-comprehensive-features.md` | Full Kotlin feature reference for competitive programming and interviews |
| `kt-labels-deep-dive` | Labeled returns, labeled breaks, and `run`/`let`/`also` scoping in algorithm code |
| `Kt-labels-deep-dive` | Non-local returns, loop labels, and chaining scope functions in complex control flow |

#### Fast I/O

| Guide | Covers |
|---|---|
| `kotlin-competitive-Programming-io.md` | Competitive programming I/O in Kotlin: fast reader, buffered writer, template structure |
| `kotlin-fastest-io.md` | Byte-buffer fast I/O: `BufferedInputStream` and the internals of `nextInt` / `nextLong` / `nextString` |

#### Concurrency

| Guide | Covers |
|---|---|
| `jvm-kotlin-concurrency-ref.md` | JVM concurrency in Kotlin: coroutines, `ReentrantLock`, `Semaphore`, LeetCode 1114 / 1115 / 1116 / 1117 |

#### Problem Walkthroughs

| Guide | Covers |
|---|---|
| `set-mismatch-guide.md` | Set Mismatch (LC 645): XOR trick, math approach, cycle detection in index mapping |
| `2073-time-needed-to-buy-tickets.md` | Time Needed to Buy Tickets (LC 2073): queue simulation vs. an O(n) closed-form solution |

---

## Problem Categories

| Category | Examples |
|---|---|
| Arrays & Strings | Two Sum, sliding window, two pointers |
| Graphs | Dijkstra, BFS/DFS, topological sort |
| Dynamic Programming | Knapsack, LIS, interval DP |
| Trees | BFS level-order, tree inversion, BST patterns |
| Backtracking | N-Queens, permutations, subsets |
| Heap / Priority Queue | Kth largest, median of a stream, top K |
| Bit Manipulation | XOR tricks, bitmask DP |
| Multithreading | Print in Order, Dining Philosophers (JVM concurrency) |

---

## Design Standards

**Naming:** expressive over terse, e.g. `settledNodes` instead of `vis`, `relaxedCost` instead of `d`.

**Style:**
- Idiomatic Kotlin stdlib (`withIndex`, `groupBy`, `fold`, `buildList`, and so on)
- Named types over raw primitives where they add clarity, e.g. `data class Edge(val to: Int, val cost: Long)`
- Numbered comments marking the phases of non-trivial algorithms
- No micro-optimizations that hide intent

**Performance:** every solution targets the asymptotically correct complexity class.
Constant-factor tuning happens only when the straightforward approach would exceed the time limit (TLE) on the given constraints.

---

## Running Solutions

Every solution is a standalone Kotlin file, so you can run it any of these ways:

```bash
# IntelliJ IDEA / Android Studio: open the file and click the run icon next to main()

# kotlinc CLI
kotlinc kotlin-solutions/743.kt -include-runtime -d 743.jar
java -jar 743.jar

# Online: paste the solution into LeetCode's Kotlin editor
```

---

> *"Code should first be understood, then optimized."*
