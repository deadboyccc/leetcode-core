package one572

class SolutionFP {
    fun diagonalSum(mat: Array<IntArray>): Int {
        val n = mat.lastIndex
        return mat.indices.sumOf { i ->
            val primary = mat[i][i]
            val secondary = mat[i][n - i]

            if (i == n - i) primary else primary + secondary
        }
    }
}

class Solution {
    fun diagonalSum(mat: Array<IntArray>): Int {
        val n = mat.lastIndex
        var sum = 0

        for (i in 0 until n) {
            // Add primary diagonal element
            sum += mat[i][i]

            // Add secondary diagonal element
            // Only if it's not the same as the primary one (avoids double counting)
            val secondaryCol = n - i
            if (secondaryCol != i) {
                sum += mat[i][secondaryCol]
            }
        }

        return sum
    }
}

fun main() {
    Solution().diagonalSum(
        arrayOf(
            intArrayOf(1, 2, 7),
            intArrayOf(3, 4, 8),
            intArrayOf(5, 6, 20),
        )
    )
}