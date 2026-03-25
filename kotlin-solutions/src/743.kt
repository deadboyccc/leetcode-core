import java.util.*

/**
 * Problem: Network Delay Time (Dijkstra's Algorithm)
 *
 * Goal: Find the minimum time for a signal from node 'k' to reach ALL 'n' nodes.
 *
 * Key Points:
 * 1. Nodes are 1-indexed (1 to n).
 * 2. Edges are directed: times[i] = [source, target, travel_time].
 * 3. Final answer is the arrival time of the last node to receive the signal.
 * 4. If any node is unreachable, return -1.
 */
class SolutionB {

    data class WeightedEdge(val source: Int, val target: Int, val travelTime: Int)

    fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {

        // 1. Data Prep: Group edges by source so we can find neighbors instantly.
        val weightedEdges = times.map { (u, v, w) -> WeightedEdge(u, v, w) }

        // 2. Setup: Priority Queue stores {targetNode, arrivalTimeAtThatNode}
        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })

        // Start at node k at time 0
        pq.add(k to 0)

        // 3. Track: Only need to know which NODES we've finalized
        val visited = hashSetOf<Int>()
        var maxTime = 0

        while (pq.isNotEmpty()) {
            val (currNode, arrivalTime) = pq.poll()

            // Skip if we've already found a faster way to this node
            if (currNode in visited) continue

            // Finalize this node
            visited.add(currNode)
            maxTime = arrivalTime

            // 4. Neighbors: How do we get from currNode to the next set of nodes?
            weightedEdges.filter { it.source == currNode }
                .forEach { edge ->
                    // Push the target node and the CUMULATIVE arrival time
                    pq.add(edge.target to (arrivalTime + edge.travelTime))
                }
        }

        // 5. Final check: Did we reach everyone?
        return if (visited.size == n) maxTime else -1
    }
}
