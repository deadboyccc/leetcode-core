package leetcode1971

/**
 * Problem: 1971. Find if Path Exists in Graph
 * * Logic:
 * 1. Build an Adjacency List using [getOrPut] for clean map initialization.
 * 2. Use BFS with [ArrayDeque] for optimal FIFO performance.
 * 3. Use early returns and functional paradigms to keep the code concise.
 */
class Solution {

    fun validPath(n: Int, edges: Array<IntArray>, source: Int, destination: Int): Boolean {
        // Trivial case: already at the target
        if (source == destination) return true

        // Build adjacency list idiomatically
        val adj = mutableMapOf<Int, MutableList<Int>>()
        for ((u, v) in edges) {
            // Using += shorthand for .add()
            adj.getOrPut(u) { mutableListOf() } += v
            adj.getOrPut(v) { mutableListOf() } += u
        }

        return hasPathBfs(source, destination, adj)
    }

    private fun hasPathBfs(start: Int, target: Int, adj: Map<Int, List<Int>>): Boolean {
        // Initialize Queue with start node using .apply scope function
        val queue = ArrayDeque<Int>().apply { addLast(start) }
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            // Safe access using Elvis operator to handle nodes with no neighbors
            for (neighbor in adj[current] ?: emptyList()) {
                // Goal reached
                if (neighbor == target) return true

                // Check if we haven't visited this neighbor yet
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor) // Mark as visited to prevent infinite loops
                    queue.addLast(neighbor)
                }
            }
        }

        // Exhausted all reachable nodes without finding the target
        return false
    }
}
