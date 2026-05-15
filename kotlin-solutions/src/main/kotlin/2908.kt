package two908

import kotlin.math.min

/**
 * LeetCode 2908: Minimum Sum of Mountain Triplets
 *
 * This snippet contains both the Brute Force (O(n²)) approach and the
 * Optimized (O(n)) approach. Both solutions ensure that for any chosen
 * peak nums[j], we find the global minimum to its left and right.
 */

class MountainTripletSolutions {

    /**
     * APPROACH 1: Brute Force
     * Strategy: Iterate through every index as a peak and slice the array to find valleys.
     * Time: O(n²) | Space: O(n)
     */
    fun bruteForce(nums: IntArray): Int {
        // Map index to value to ensure every position is checked, even with duplicates.
        val idxToValue = nums.indices.associateWith { nums[it] }
        var minSum = Int.MAX_VALUE

        for ((idx, num) in idxToValue) {
            // Find the minimums strictly to the left and right of the current index.
            val leftSideMin = nums.slice(0 until idx).minOrNull()
            val rightSideMin = nums.slice(idx + 1 until nums.size).minOrNull()

            // Mountain condition: peak must be strictly greater than neighbors.
            if (leftSideMin != null && rightSideMin != null &&
                num > leftSideMin && num > rightSideMin
            ) {
                minSum = minOf(minSum, num + leftSideMin + rightSideMin)
            }
        }

        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }

    /**
     * APPROACH 2: Optimized Suffix Precomputation
     * Strategy: Pre-calculate the smallest values on the right to avoid nested loops.
     * Time: O(n) | Space: O(n)
     */
    fun optimized(nums: IntArray): Int {
        val n = nums.size
        if (n < 3) return -1

        // --- 1. Precompute Suffix Minimums (Right Side) ---
        // rightMin[i] is the smallest value from index i to the end of the array.
        val rightMin = IntArray(n)
        rightMin[n - 1] = nums[n - 1]
        for (i in n - 2 downTo 0) {
            rightMin[i] = min(nums[i], rightMin[i + 1])
        }

        // --- 2. Single Pass for Peak (Left Side) ---
        var leftMin = nums[0] // Running minimum of nums[0...j-1]
        var minSum = Int.MAX_VALUE
        var found = false

        // A peak must have at least one element to its left (j=1) and right (j=n-2).
        for (j in 1 until n - 1) {
            val peak = nums[j]
            val bestRightValley = rightMin[j + 1]

            // Check if this peak forms a valid mountain with the best valleys found.
            if (peak > leftMin && peak > bestRightValley) {
                minSum = min(minSum, peak + leftMin + bestRightValley)
                found = true
            }

            // Update leftMin for the next potential peak.
            leftMin = min(leftMin, peak)
        }

        return if (found) minSum else -1
    }
}