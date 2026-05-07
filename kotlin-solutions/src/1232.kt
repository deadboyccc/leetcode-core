package one232

class Solution {
    fun checkStraightLine(coordinates: Array<IntArray>): Boolean {
        // any two points = a line
        if (coordinates.size <= 2) return true

        // Delta X - Delta Y for first two points
        val dx0 = coordinates[1][0] - coordinates[0][0]
        val dy0 = coordinates[1][1] - coordinates[0][1]

        // Use 'all' to verify the cross-multiplication property for every point
        return coordinates.drop(2).all { (x, y) ->
            // Delta X - Delta Y [ This point to first Point ]
            val dxi = x - coordinates[0][0]
            val dyi = y - coordinates[0][1]
            // check if slope matches in cross-multiplication formula to avoid div by 0
            dy0 * dxi == dyi * dx0
        }
    }
}
