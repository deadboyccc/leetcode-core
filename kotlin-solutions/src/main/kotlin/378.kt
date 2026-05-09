package three78

import java.util.*

/*
========================================================
Kth Smallest Element in a Sorted Matrix
========================================================

Matrix properties:
- rows sorted ascending
- columns sorted ascending

We show 2 optimal approaches:

1. Binary Search on VALUE RANGE
   - Best overall asymptotic solution
   - O(n * log(valueRange))
   - O(1) space

2. Min Heap / K-Way Merge
   - Great when k is small
   - O(k log n)
   - O(n) space
*/


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
        matrix: Array<IntArray>,
        target: Int
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


// ========================================================
// 2. Min Heap / K-Way Merge
// ========================================================

/*
Time:
O(k log n)

Why?
- heap size is at most n
- we do k heap removals

Space:
O(n)

Core Idea:
Treat each row as a sorted list.

Put first element from every row into heap.

Always remove the global smallest.

After removing:
insert next element from same row.
*/

class SolutionHeap {

    data class Element(
        val value: Int,
        val row: Int,
        val col: Int
    )

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        val n = matrix.size

        // Min-heap ordered by the element's value
        // Initializing with k to avoid unnecessary resizing if k < n
        val minHeap = PriorityQueue<Element>(compareBy { it.value })

        // 1. Add the first element of every row to initialize the heap
        for (row in 0 until n) {
            minHeap.offer(Element(matrix[row][0], row, 0))
        }

        // 2. Remove k - 1 smallest elements from the heap
        repeat(k - 1) {
            val current = minHeap.poll() ?: return@repeat

            val nextCol = current.col + 1

            // 3. If there is a next element in the same row, add it to the heap
            if (nextCol < n) {
                minHeap.offer(
                    Element(matrix[current.row][nextCol], current.row, nextCol)
                )
            }
        }

        // 4. The k-th smallest element is now at the top of the heap
        return minHeap.peek().value
    }
}

// ========================================================
// Another GPT Solution
// ========================================================
class Solution {

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
        val n = matrix.size

        var left = matrix[0][0]
        var right = matrix[n - 1][n - 1]

        while (left < right) {
            val mid = left + (right - left) / 2

            if (countLessOrEqual(matrix, mid) < k) {
                left = mid + 1
            } else {
                right = mid
            }
        }

        return left
    }

    /**
     * Counts how many numbers are <= target
     *
     * Time: O(n)
     * Space: O(1)
     */
    private fun countLessOrEqual(
        matrix: Array<IntArray>,
        target: Int
    ): Int {

        val n = matrix.size

        var row = n - 1
        var col = 0

        var count = 0

        while (row >= 0 && col < n) {

            if (matrix[row][col] <= target) {

                // Everything above current cell
                // in this column is also <= target
                count += row + 1

                // Move right
                col++

            } else {

                // Current value too large
                // Move upward to smaller values
                row--
            }
        }

        return count
    }
}

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
// ========================================================
// Main
// ========================================================

fun main() {

    val matrix = arrayOf(
        intArrayOf(1, 5, 9),
        intArrayOf(10, 11, 13),
        intArrayOf(12, 13, 15)
    )

    val k = 8

    println(
        SolutionBinarySearch()
            .kthSmallest(matrix, k)
    )

    println(
        SolutionHeap()
            .kthSmallest(matrix, k)
    )


    // ====================================================
    // Kotlin coercion helpers
    // ====================================================

    val a = 10
    a.coerceAtMost(5)
        .also(::println) // min -> 5

    val b = 5
    b.coerceAtLeast(10)
        .also(::println) // max -> 10

    val c = 3
    c.coerceIn(1..5)
        .also(::println) // clamp -> 3
}
