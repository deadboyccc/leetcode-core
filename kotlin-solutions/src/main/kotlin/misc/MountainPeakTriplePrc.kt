package misc.mountainPeakTriplePrc.practice

class PeakOptimization {
    class Solution {
        fun minimumSum(nums: IntArray): Int {
            val n = nums.size
            if (n < 3) return -1

            val rightMin = IntArray(n)
            rightMin[n - 1] = nums[n - 1]
            for (i in n - 2 downTo 2) {
                val v = nums[i]
                val next = rightMin[i + 1]
                rightMin[i] = if (v < next) v else next
            }

            var leftMin = nums[0]
            var minSum = Int.MAX_VALUE

            for (j in 1 until n - 1) {
                val peak = nums[j]
                val rightVal = rightMin[j + 1]

                if (peak > leftMin && peak > rightVal) {
                    val currentSum = peak + leftMin + rightVal
                    if (currentSum < minSum) minSum = currentSum
                }

                if (peak < leftMin) leftMin = peak
            }

            return if (minSum == Int.MAX_VALUE) -1 else minSum
        }
    }
}

class Solution {
    fun minimumSum(nums: IntArray): Int {
        val n = nums.size

        // Need at least 3 elements to form a triplet (i, j, k).
        if (n < 3) return -1

        // --- Precompute suffix minimums ---
        // rightMin[i] = minimum of nums[i..n-1]
        // Build right→left so each entry answers "what's the smallest
        // element from here to the end?"
        val rightMin = IntArray(n)
        rightMin[n - 1] = nums[n - 1]           // base case: last element
        for (i in n - 2 downTo 0) {
            rightMin[i] = minOf(nums[i], rightMin[i + 1])
        }
        // After this loop:
        //   rightMin[0] = global min of the whole array
        //   rightMin[n-1] = nums[n-1]

        // --- Single left-to-right pass over candidate peaks ---
        // leftMin tracks the running minimum of nums[0..j-1].
        // Initialized to nums[0] before the loop starts at j=1.
        var leftMin = nums[0]

        var minSum = Int.MAX_VALUE

        // j ranges over valid peak positions: must have at least one
        // element to the left (j ≥ 1) and one to the right (j ≤ n-2).
        for (j in 1 until n - 1) {
            val peak = nums[j]

            // rightMin[j+1] = minimum of nums[j+1..n-1], i.e. the best
            // right-valley we can pair with this peak.
            val rightVal = rightMin[j + 1]

            // Mountain condition: peak must be strictly greater than
            // both the best left-valley and the best right-valley.
            if (peak > leftMin && peak > rightVal) {
                minSum = minOf(minSum, peak + leftMin + rightVal)
            }

            // Expand the left window: include nums[j] for future peaks.
            // This must happen AFTER the sum check so that leftMin for
            // index j is truly the minimum of nums[0..j-1], not nums[0..j].
            leftMin = minOf(leftMin, peak)
        }

        return if (minSum == Int.MAX_VALUE) -1 else minSum
    }
}
