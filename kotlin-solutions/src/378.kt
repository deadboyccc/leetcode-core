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

    data class Node(
        val value: Int,
        val row: Int,
        val col: Int
    )

    fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {

        val n = matrix.size

        // min heap ordered by value
        val minHeap = PriorityQueue<Node> { a, b ->
            a.value - b.value
        }

        // add first element of every row
        for (row in 0 until n) {
            minHeap.offer(
                Node(
                    matrix[row][0],
                    row,
                    0
                )
            )
        }

        // remove k - 1 smallest elements
        repeat(k - 1) {

            val current = minHeap.poll()

            val nextCol = current.col + 1

            // add next element from same row
            if (nextCol < n) {
                minHeap.offer(
                    Node(
                        matrix[current.row][nextCol],
                        current.row,
                        nextCol
                    )
                )
            }
        }

        return minHeap.peek().value
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
