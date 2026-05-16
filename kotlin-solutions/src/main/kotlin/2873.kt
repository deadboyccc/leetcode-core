package two873

import kotlin.math.max

// imo impossible to come up with on the fly xD
class SolutionOOne {

    class Solution {
        fun maximumTripletValue(nums: IntArray): Long {
            var maxTriplet = 0L
            var maxElement = 0L
            var maxDiff = 0L

            for (num in nums) {
                maxTriplet = max(maxTriplet, maxDiff * num)
                maxDiff = max(maxDiff, maxElement - num)
                maxElement = max(maxElement, num.toLong())
            }

            return maxTriplet
        }
    }
}

// Brute Force
class Solution {

    // i<j<k
    // (num[i]-num[j])*num[k]
    // return max val is not >0 return 0
    fun maximumTripletValue(nums: IntArray): Long {
        var maxVal = Long.MIN_VALUE / 2

        for (i in nums.indices) {
            for (j in i + 1 until nums.size) {
                for (k in j + 1 until nums.size) {
                    maxVal = maxOf(maxVal, calcValue(nums[i], nums[j], nums[k]))
                }
            }
        }

        return if (maxVal > 0) maxVal else 0

    }

    inline fun calcValue(iVal: Int, jVal: Int, kVal: Int): Long = (iVal.toLong() - jVal.toLong()) * kVal.toLong()
}
