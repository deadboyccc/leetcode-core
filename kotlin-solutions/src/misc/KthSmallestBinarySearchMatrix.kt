package misc.practice.kthSmallestBSM
// ========================================================
// 1. Binary Search + Staircase Counting
// ========================================================

/*
Time:
O(n * log(valueRange))

Why?
- Binary search over possible values
- Each counting pass costs O(n)

Space:
O(1)

Core Idea:
Pick a value mid.

Count how many numbers are <= mid.

If count < k:
    kth smallest is bigger.

Else:
    answer could still be mid or smaller.
*/
class PracticeSolution {
    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        var left = matrix.first().first()
        var right = matrix.last().last()
        val targetLessOrEqual = k - 1

        while (left < right) {
            // "Right-Biased" mid: when (left, right) are neighbors, this picks the higher one
            // we are closing towards right, in the end if mid = right, then we check left=mid , and return the left
            // which is mid which is at the right 
            // This prevents the infinite loop when setting left = mid
            val mid = right - (right - left) / 2

            // We count how many elements are STRICTLY LESS than mid
            // If that count is <= target (k-1), then mid could be our answer or higher
            if (countStrictlyLess(matrix, mid) <= targetLessOrEqual) {
                left = mid
            } else {
                right = mid - 1
            }
        }
        return left // which is mid
    }

    private fun countStrictlyLess(matrix: Array<IntArray>, target: Int): Int {
        var count = 0
        var row = matrix.size - 1
        var col = 0
        while (row >= 0 && col < matrix[0].size) {
            if (matrix[row][col] < target) {
                count += (row + 1)
                col++
            } else {
                row--
            }
        }
        return count
    }
}

class SolutionBinarySearch {

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {

        var left = matrix[0][0]
        var right = matrix.last().last()

        while (left < right) {

            // overflow safe midpoint
            val mid = left + (right - left) / 2

            val count = countLessOrEqual(matrix, mid)

            if (count < k) {
                left = mid + 1
            } else {
                right = mid
            }
        }

        return left
    }

    /*
    Staircase Counting
    ------------------

    Time:
    O(n)

    Start bottom-left.

    Why?

    - moving RIGHT increases values
    - moving UP decreases values

    So each move eliminates
    either a row or a column.

    Example:

      1   5   9
     10  11  13
     12  13  15

    target = 13

    Start at 12:
    12 <= 13

    Everything above it is also <= 13:
      1
     10
     12

    Add 3 instantly.
    */
    private fun countLessOrEqual(
        matrix: Array<IntArray>, target: Int
    ): Int {

        val n = matrix.size

        var row = n - 1
        var col = 0

        var count = 0

        while (row >= 0 && col < n) {

            if (matrix[row][col] <= target) {

                // all values above are also <= target
                count += row + 1

                col++
            } else {
                row--
            }
        }
        return count
    }
}
