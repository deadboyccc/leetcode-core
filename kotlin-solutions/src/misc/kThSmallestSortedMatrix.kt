package misc.kThSmallestSortedMatrix.practice.one

class SimilarBinarySearchSolution {
    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix[0][0]
        var right = matrix.last().last()
        var ans = right // Standard "best candidate" variable

        while (left <= right) { // Typical <= condition
            val mid = left + (right - left) / 2

            if (countLessOrEqual(matrix, mid) >= k) {
                ans = mid      // This mid works, but there might be a smaller one
                right = mid - 1 // Try to find a smaller value in the left half
            } else {
                left = mid + 1  // mid is too small, search the right half
            }
        }
        return ans
    }

    private fun countLessOrEqual(matrix: Array<IntArray>, target: Int): Int {
        val n = matrix.size

        var row = n - 1
        var col = 0

        var count = 0

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += row + 1
                col++
            } else row--
        }
        return count
    }

}

class Solution {
    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix[0][0]
        var right = matrix.last().last()

        while (left < right) {
            val mid = left + (right - left) / 2
            // if the element before mid are less than k
            // we want the kth so it has to be k elements before it
            // looking for 30 -> mid = 17, target>17
            // [ 10, 15,17,20,30]
            if (countLessOrEqual(matrix, mid) < k) {
                left = mid + 1
            } else {
                right = mid
            }
        }
        return left
    }

    private fun countLessOrEqual(matrix: Array<IntArray>, target: Int): Int {
        val n = matrix.size

        var row = n - 1
        var col = 0

        var count = 0

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += row + 1
                col++
            } else row--
        }
        return count
    }
}

class BinarySearch {
    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix[0][0]
        var right = matrix.last().last()
        var ans = right // Standard "best candidate" variable

        while (left <= right) { // Typical <= condition
            val mid = left + (right - left) / 2

            if (countLessOrEqual(matrix, mid) >= k) {
                ans = mid      // This mid works, but there might be a smaller one
                right = mid - 1 // Try to find a smaller value in the left half
            } else {
                left = mid + 1  // mid is too small, search the right half
            }
        }
        return ans

    }

    private fun countLessOrEqual(matrix: Array<IntArray>, target: Int): Int {
        val n = matrix.size

        var row = n - 1
        var col = 0

        var count = 0

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += row + 1
                col++
            } else row--
        }
        return count
    }
}