package leetcode1971

/**
 * Problem: 1971. Find if Path Exists in Graph
 * * Logic:
 * 1. Build an Adjacency List using [getOrPut] for clean map initialization.
 * 2. Use BFS with [ArrayDeque] for optimal FIFO performance.
 * 3. Use early returns and functional paradigms to keep the code concise.
 */
class DfsSolution {
    fun validPath(n: Int, edges: Array<IntArray>, source: Int, destination: Int): Boolean {
        if (source == destination) return true

        // 1. Use an Array of Lists for better performance than a Map
        val adj = Array(n) { mutableListOf<Int>() }
        for ((u, v) in edges) {
            adj[u].add(v)
            adj[v].add(u)
        }

        // 2. Use a BooleanArray for O(1) lookup and better memory efficiency
        val visited = BooleanArray(n)

        return hasPathDfs(source, destination, adj, visited)
    }

    private fun hasPathDfs(
        current: Int,
        target: Int,
        adj: Array<MutableList<Int>>,
        visited: BooleanArray
    ): Boolean {
        if (current == target) return true
        visited[current] = true

        // 3. Use .any { ... } to idiomatically search for a valid path
        return adj[current].any { neighbor ->
            !visited[neighbor] && hasPathDfs(neighbor, target, adj, visited)
        }
    }
}

class DfsSolutionWithoutAny {
    fun validPath(n: Int, edges: Array<IntArray>, source: Int, destination: Int): Boolean {
        if (source == destination) return true

        // Use a BooleanArray for performance; use n to size it
        val visited = BooleanArray(n)

        // Idiomatic adjacency list construction
        val adj = Array(n) { mutableListOf<Int>() }
        for ((u, v) in edges) {
            adj[u] += v
            adj[v] += u
        }

        return hasPathDfs(source, destination, adj, visited)
    }

    private fun hasPathDfs(
        start: Int,
        target: Int,
        adj: Array<MutableList<Int>>,
        visited: BooleanArray
    ): Boolean {
        if (start == target) return true

        visited[start] = true // Mark current node as visited

        // Iterate through neighbors manually without .any()
        for (neighbor in adj[start]) {
            if (!visited[neighbor]) {
                // If this neighbor's path finds the target, return true
                // If NOT, the loop continues to check the next neighbor
                if (hasPathDfs(neighbor, target, adj, visited)) {
                    return true
                }
            }
        }

        // If no neighbors lead to the target
        return false
    }
}
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
