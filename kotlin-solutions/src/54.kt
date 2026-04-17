package five4

class Solution {
    fun spiralOrder(matrix: Array<IntArray>): List<Int> {
        var top = 0;
        var bottom = matrix.lastIndex
        var left = 0;
        var right = matrix[0].lastIndex
        val result = mutableListOf<Int>()

        while (top <= bottom && left <= right) {

            // traverse top row left → right, then shrink top boundary down
            for (i in left..right) result.add(matrix[top][i])
            top++

            // traverse right column top → bottom, then shrink right boundary left
            for (i in top..bottom) result.add(matrix[i][right])
            right--

            // traverse bottom row right → left (only if a row still remains), then shrink bottom up
            if (top <= bottom) for (i in right downTo left) result.add(matrix[bottom][i])
            bottom--

            // traverse left column bottom → top (only if a column still remains), then shrink left right
            if (left <= right) for (i in bottom downTo top) result.add(matrix[i][left])
            left++
        }

        return result
    }
}

class SolutionRecursive {
    fun spiralOrder(matrix: Array<IntArray>): List<Int> {
        val result = mutableListOf<Int>()

        fun peel(top: Int, bottom: Int, left: Int, right: Int) {
            if (top > bottom || left > right) return

            // top row
            for (i in left..right) result.add(matrix[top][i])

            // right column
            for (i in top + 1..bottom) result.add(matrix[i][right])

            // bottom row (only if a separate bottom row exists)
            if (top < bottom) for (i in right - 1 downTo left) result.add(matrix[bottom][i])

            // left column (only if a separate left column exists)
            if (left < right) for (i in bottom - 1 downTo top + 1) result.add(matrix[i][left])

            // peel one ring off, recurse into the inner matrix
            peel(top + 1, bottom - 1, left + 1, right - 1)
        }

        peel(0, matrix.lastIndex, 0, matrix[0].lastIndex)
        return result
    }
}