import java.util.*
import kotlin.math.abs

/**
 * Problem: Min Cost to Connect All Points (Minimum Spanning Tree)
 *
 * Goal: Connect all 'n' points such that the total distance is minimized.
 *
 * Key Points:
 * 1. Points are (x, y) coordinates on a 2D grid.
 * 2. Distance is Manhattan Distance: |x1 - x2| + |y1 - y2|.
 * 3. All points must be connected (directly or indirectly).
 * 4. Return the minimum sum of edge weights.
 */

class SolutionA {
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
