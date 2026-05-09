package misc.DsaUtils

class TopologicalSort {

    // ─── DFS-based Topological Sort ───────────────────────────────────────────

    fun dfsSort(adjacencyList: Array<IntArray>, nodeCount: Int): List<Int> {
        val visited = BooleanArray(nodeCount)
        val sortedNodes = ArrayDeque<Int>()

        // Visit every node to handle disconnected graphs
        adjacencyList.indices.forEach { node ->
            if (!visited[node]) dfs(node, adjacencyList, visited, sortedNodes)
        }

        return sortedNodes.toList()
    }

    // Post-order DFS: prepend node only after all its descendants are fully explored
    private fun dfs(
        node: Int,
        adjacencyList: Array<IntArray>,
        visited: BooleanArray,
        sortedNodes: ArrayDeque<Int>
    ) {
        visited[node] = true

        // Recurse into every unvisited neighbor first
        adjacencyList[node].forEach { neighbor ->
            if (!visited[neighbor]) dfs(neighbor, adjacencyList, visited, sortedNodes)
        }

        // All descendants settled — safe to place this node at the front
        sortedNodes.addFirst(node)
    }

    // ─── Kahn's BFS-based Topological Sort ────────────────────────────────────

    fun kahnSort(adjacencyList: Array<IntArray>, nodeCount: Int): List<Int> {
        // Count how many edges point INTO each node
        val inDegree = IntArray(nodeCount).also { degree ->
            adjacencyList.indices.forEach { source ->
                adjacencyList[source].forEach { destination -> degree[destination]++ }
            }
        }

        // Nodes with no incoming edges have no dependencies — process them first
        val readyQueue = ArrayDeque<Int>().also { queue ->
            inDegree.indices
                .filter { node -> inDegree[node] == 0 }
                .forEach { node -> queue.add(node) }
        }

        val sortedNodes = mutableListOf<Int>()

        while (readyQueue.isNotEmpty()) {
            val currentNode = readyQueue.removeFirst()
            sortedNodes.add(currentNode)

            // Remove this node's contribution to its neighbors' in-degrees
            // If a neighbor is now unblocked (in-degree hits 0), it's ready to process
            adjacencyList[currentNode].forEach { neighbor ->
                if (--inDegree[neighbor] == 0) readyQueue.add(neighbor)
            }
        }

        return sortedNodes
    }

    // ─── Cycle Detection ───────────────────────────────────────────────────────

    // DFS cycle detection
    // Tracks nodes on the active call stack — a back-edge into the current path means a cycle
    fun hasCycleDfs(adjacencyList: Array<IntArray>, nodeCount: Int): Boolean {
        val visited = BooleanArray(nodeCount)
        val onCurrentPath = BooleanArray(nodeCount)

        return adjacencyList.indices.any { node ->
            !visited[node] && dfsCycleCheck(node, adjacencyList, visited, onCurrentPath)
        }
    }

    private fun dfsCycleCheck(
        node: Int,
        adjacencyList: Array<IntArray>,
        visited: BooleanArray,
        onCurrentPath: BooleanArray
    ): Boolean {
        visited[node] = true
        onCurrentPath[node] = true  // entering this node's path

        val cycleFound = adjacencyList[node].any { neighbor ->
            // Already on our current path → back-edge → cycle
            // Not yet visited → recurse and check deeper
            onCurrentPath[neighbor] ||
                    (!visited[neighbor] && dfsCycleCheck(neighbor, adjacencyList, visited, onCurrentPath))
        }

        onCurrentPath[node] = false // leaving this node's path on backtrack
        return cycleFound
    }

    // Kahn cycle detection
    // Nodes trapped in a cycle never reach in-degree 0 so they're never enqueued
    // If sorted result is smaller than nodeCount, some nodes were never processed → cycle exists
    fun hasCycleKahn(adjacencyList: Array<IntArray>, nodeCount: Int): Boolean =
        kahnSort(adjacencyList, nodeCount).size != nodeCount
}
