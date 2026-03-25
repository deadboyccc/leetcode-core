import java.util.*

class SolutionB {

    data class WeightedEdge(val target: Int, val travelTime: Int)
    data class State(val time: Int, val node: Int)

    fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {

        // 1. Build adjacency map (unchanged)
        val graph = times
            .map { (u, v, w) -> u to WeightedEdge(v, w) }
            .groupBy({ it.first }, { it.second })

        // 2. Min-heap using State instead of Pair
        val pq = PriorityQueue<State>(compareBy { it.time })
        pq.add(State(0, k))

        val visited = HashSet<Int>()
        var maxTime = 0

        while (pq.isNotEmpty()) {
            val (arrivalTime, currNode) = pq.poll()

            if (currNode in visited) continue

            visited.add(currNode)
            maxTime = arrivalTime

            graph[currNode]?.forEach { edge ->
                if (edge.target !in visited) {
                    pq.add(State(arrivalTime + edge.travelTime, edge.target))
                }
            }
        }

        return if (visited.size == n) maxTime else -1
    }
}