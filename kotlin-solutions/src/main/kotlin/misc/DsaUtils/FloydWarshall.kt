const val INF = Long.MAX_VALUE / 2

/**
 * Computes shortest paths between all pairs of nodes using Floyd-Warshall.
 */
fun floydWarshall(initialDistances: Array<LongArray>): Array<LongArray> =
    initialDistances.indices.fold(initialDistances) { currentMatrix, intermediate ->
        Array(currentMatrix.size) { source ->
            LongArray(currentMatrix.size) { destination ->
                val currentPath = currentMatrix[source][destination]
                val pathViaIntermediate = currentMatrix[source][intermediate] + currentMatrix[intermediate][destination]

                // Update if the detour through intermediate node is shorter and valid
                if (currentMatrix[source][intermediate] < INF && currentMatrix[intermediate][destination] < INF) {
                    minOf(currentPath, pathViaIntermediate)
                } else {
                    currentPath
                }
            }
        }
    }

/**
 * Transforms edge list into an adjacency matrix, keeping only the shortest edges.
 */
fun buildMatrix(nodeCount: Int, edges: List<Triple<Int, Int, Long>>): Array<LongArray> {
    // Group by source-to-destination and pick the minimum weight for each pair
    val shortestDirectEdges = edges
        .groupBy({ it.first to it.second }, { it.third })
        .mapValues { (_, weights) -> weights.min() }

    return Array(nodeCount) { source ->
        LongArray(nodeCount) { destination ->
            when {
                source == destination -> 0L // Distance to self is always zero
                else -> shortestDirectEdges[source to destination] ?: INF // Use weight or INF if no edge exists
            }
        }
    }
}

/**
 * Checks if a node can reach itself with a negative total distance.
 */
fun hasNegativeCycle(finalDistances: Array<LongArray>): Boolean =
    finalDistances.indices.any { node -> finalDistances[node][node] < 0L }
