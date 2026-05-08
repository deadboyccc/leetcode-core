package three73

/*
         nums2 →
   num1   b1   b2   b3
    a1   a1+b1 a1+b2 a1+b3
    a2   a2+b1 a2+b2 a2+b3
    a3   a3+b1 a3+b2 a3+b3
*/

import java.util.*

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
// OPTIMAL
// Time: O(k log min(k, n))
// Space: O(min(k, n))
// ======================================================

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

            repeat(minOf(k, nums1.size)) { heap += it to 0 }

            val limit = minOf(k.toLong(), nums1.size.toLong() * nums2.size.toLong()).toInt()

            return buildList {
                repeat(limit) {

                    val (i, j) = heap.poll()

                    add(listOf(nums1[i], nums2[j]))

                    if (j + 1 < nums2.size) heap += i to (j + 1)
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
