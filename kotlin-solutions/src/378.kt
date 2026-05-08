package three78

// Matrix is sorted:
// - each row ascending
// - each column ascending
//
// Binary search the VALUE RANGE, not the indices.
//
// Idea:
// If we pick a value `mid`,
// we can count how many numbers in the matrix are <= mid.
//
// Example:
// if count = 5
// then mid is at least the 5th smallest.
//
// So:
// - if count < k  -> answer is bigger
// - else          -> answer could still be mid or smaller
//
// Time:
// O(rows * log(cols) * log(valueRange))
//
// Space:
// O(1)
class Solution {

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix[0][0]
        var right = matrix.last().last()

        while (left < right) {

            // avoid overflow
            val mid = left + (right - left) / 2

            var count = 0

            // count how many numbers are <= mid
            for (row in matrix) {
                count += upperBound(row, mid)
            }

            // not enough smaller numbers
            // kth element must be bigger
            if (count < k) {
                left = mid + 1
            } else {
                // mid might already be the answer
                right = mid
            }
        }

        return left
    }

    // first index where value > target
    // also equals count of values <= target
    private fun upperBound(row: IntArray, target: Int): Int {
        var left = 0
        var right = row.size

        while (left < right) {
            val mid = left + (right - left) / 2

            if (row[mid] <= target) {
                left = mid + 1
            } else {
                right = mid
            }
        }

        return left
    }
}

fun main() {

    val matrix = arrayOf(
        intArrayOf(1, 5, 9),
        intArrayOf(10, 11, 13),
        intArrayOf(12, 13, 15)
    )

    println(Solution().kthSmallest(matrix, 8)) // 13


    // experimenting with coercion helpers

    val a = 10
    a.coerceAtMost(5).also(::println) // min(10, 5) -> 5

    val b = 5
    b.coerceAtLeast(10).also(::println) // max(5, 10) -> 10

    val c = 3
    c.coerceIn(1..5).also(::println) // clamp into range -> 3
}