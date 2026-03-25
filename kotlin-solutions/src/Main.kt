import java.util.*
import kotlin.math.abs

class Solution {
    data class Point(val x: Int, val y: Int)

    // Extension function makes the call site cleaner: p1.distTo(p2)
    private fun Point.distTo(other: Point) = abs(x - other.x) + abs(y - other.y)

    fun minCostConnectPoints(points: Array<IntArray>): Int {
        val allPoints = points.map { (x, y) -> Point(x, y) }
        val n = allPoints.size

        var totalCost = 0
        val visited = mutableSetOf<Point>()

        // Use a min-heap; Pair(distance, point)
        val pq = PriorityQueue<Pair<Int, Point>>(compareBy { it.first })
        pq.add(0 to allPoints[0])

        while (visited.size < n && pq.isNotEmpty()) {
            val (dist, curr) = pq.poll()

            if (!visited.add(curr)) continue // add() returns false if item already exists

            totalCost += dist

            // Use sequences to avoid creating intermediate lists with filter
            allPoints.asSequence()
                .filter { it !in visited }
                .forEach { next -> pq.add(curr.distTo(next) to next) }
        }

        return totalCost
    }
}
