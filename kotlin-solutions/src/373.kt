package three73

import java.util.*

/*
         nums2 →
   num1   b1   b2   b3
    a1   a1+b1 a1+b2 a1+b3
    a2   a2+b1 a2+b2 a2+b3
    a3   a3+b1 a3+b2 a3+b3
*/

class OptimalWithReadability {

    // ======================================================
// OPTIMAL: Min-Heap approach
// Time: O(k log min(k, n))
// Space: O(min(k, n))
// ======================================================
    class Solution {
        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {
            if (nums1.isEmpty() || nums2.isEmpty() || k == 0) return emptyList()

            // Min-heap stores indices [i, j], ordered by the sum of nums1[i] + nums2[j]
            val heap = PriorityQueue<Pair<Int, Int>>(compareBy { (i, j) -> nums1[i] + nums2[j] })

            /*
                     nums2 →
               num1   b1   b2   b3
                a1   a1+b1 a1+b2 a1+b3
                a2   a2+b1 a2+b2 a2+b3
                a3   a3+b1 a3+b2 a3+b3
            */
            // say k = 4, so minOf(4,3) = 3  -> i in 1..3
            // Initialize heap with the first element of nums2 paired with up to k elements from nums1
            repeat(minOf(k, nums1.size)) { i ->
                heap.add(i to 0)
            }

            return buildList {
                // Extract the smallest sum and potentially add the next pair in the "row"
                repeat(minOf(k, heap.size + size)) { // Ensures we don't loop more than available pairs
                    if (heap.isEmpty()) return@repeat

                    val (i, j) = heap.poll()
                    add(listOf(nums1[i], nums2[j]))

                    // If there's a next element in nums2 for this specific nums1[i], add it to heap
                    if (j + 1 < nums2.size) {
                        heap.add(i to (j + 1))
                    }

                    if (this.size == k) return@buildList
                }
            }
        }
    }
}

// ======================================================
// OPTIMAL
// Time: O(k log min(k, n))
// Space: O(min(k, n))
// ======================================================
class Optimal {
    class Solution {

        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {

            if (nums1.isEmpty() || nums2.isEmpty() || k == 0) return emptyList()

            val heap = PriorityQueue<Pair<Int, Int>>(compareBy { (i, j) -> nums1[i] + nums2[j] })
            /*
                     nums2 →
               num1   b1   b2   b3
                a1   a1+b1 a1+b2 a1+b3
                a2   a2+b1 a2+b2 a2+b3
                a3   a3+b1 a3+b2 a3+b3
            */

            // we add the first col k times or num1.size ( min of them )
            repeat(minOf(k, nums1.size)) { heap += it to 0 }

            // the limit is necessary in cases where the problem asks for say 10 pairs k=10
            // the size of the matrix itself is num1.size* num2.size could be less
            // say that the size of the matrix num1.size*num2.size = 4, so we can only give 4
            val limit = minOf(k.toLong(), nums1.size.toLong() * nums2.size.toLong()).toInt()

            // we build the list, repeating as many as the limit we sat earlier
            return buildList {
                repeat(limit) {

                    // indices of the smallest
                    val (i, j) = heap.poll()

                    // add the num pairs
                    add(listOf(nums1[i], nums2[j]))

                    // if that smallest element has a neighboring col (-> right of it in its row)
                    // add it too
                    // same row = same i to (j+1)-> same row advance 1 col right
                    if (j + 1 < nums2.size) heap += i to (j + 1)
                }
            }
        }
    }
}

// ======================================================
// slightly better space complexity as we pick the smallest list to min the heap with ( negligible + added complexity )
// ======================================================
class SpaceOptimized {
    class Solution {
        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {
            if (nums1.isEmpty() || nums2.isEmpty() || k == 0) return emptyList()

            // Ensure 'a' is the smaller array to keep the PriorityQueue size minimal
            val swapped = nums1.size > nums2.size
            val (a, b) = if (!swapped) nums1 to nums2 else nums2 to nums1

            val heap = PriorityQueue<Pair<Int, Int>>(compareBy { (i, j) -> a[i] + b[j] })

            // Initialize heap with the first possible pair from each "row" of the smaller array
            repeat(minOf(k, a.size)) { heap += it to 0 }

            val limit = minOf(k.toLong(), a.size.toLong() * b.size.toLong()).toInt()

            return buildList {
                repeat(limit) {
                    if (heap.isNotEmpty()) {
                        val (i, j) = heap.poll()

                        // Restore original (nums1, nums2) order if arrays were swapped for optimization
                        if (swapped) {
                            add(listOf(b[j], a[i]))
                        } else {
                            add(listOf(a[i], b[j]))
                        }

                        // Move to the next element in array 'b' for the current element in array 'a'
                        if (j + 1 < b.size) heap += i to (j + 1)
                    }
                }
            }
        }
    }
}


// ======================================================
// SAME IDEA — explicit sum storage
// ======================================================
class VerboseHeap {

    class Solution {

        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {

            if (nums1.isEmpty() || nums2.isEmpty() || k == 0) return emptyList()

            val heap = PriorityQueue<Triple<Int, Int, Int>>(compareBy { it.first })

            repeat(minOf(k, nums1.size)) { i ->
                heap += Triple(nums1[i] + nums2[0], i, 0)
            }

            val limit = minOf(k.toLong(), nums1.size.toLong() * nums2.size.toLong()).toInt()

            return buildList {
                repeat(limit) {

                    val (_, i, j) = heap.poll()

                    add(listOf(nums1[i], nums2[j]))

                    if (j + 1 < nums2.size)
                        heap += Triple(nums1[i] + nums2[j + 1], i, j + 1)
                }
            }
        }
    }
}

// ======================================================
// BRUTE FORCE
// Time: O(n*m log(n*m))
// Space: O(n*m)
// ======================================================
class BruteForce {

    class Solution {

        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {

            if (nums1.isEmpty() || nums2.isEmpty() || k == 0) return emptyList()

            val heap = PriorityQueue<Pair<Int, Int>>(compareBy { it.first + it.second })

            for (n1 in nums1)
                for (n2 in nums2)
                    heap += n1 to n2

            val limit = minOf(k.toLong(), nums1.size.toLong() * nums2.size.toLong()).toInt()

            return buildList {
                repeat(limit) {

                    val (n1, n2) = heap.poll()

                    add(listOf(n1, n2))
                }
            }
        }
    }
}

// ======================================================
// WRONG GREEDY APPROACH
// Local greedy != global minimum
// ======================================================
class WrongGreedy {

    class Solution {

        private var i = 0
        private var j = 0

        fun kSmallestPairs(nums1: IntArray, nums2: IntArray, k: Int): List<List<Int>> {

            val result = mutableListOf<List<Int>>()

            repeat(k) {
                if (nums1[i] < nums2[j])
                    result += listOf(nums1[i], nums2[j++])
                else
                    result += listOf(nums1[i++], nums2[j])
            }

            return result
        }
    }
}
