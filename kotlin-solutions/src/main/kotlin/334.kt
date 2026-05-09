package three34;

class Solution {
    fun increasingTriplet(nums: IntArray): Boolean {

        var first = Int.MAX_VALUE
        var second = Int.MAX_VALUE

        for (num in nums) {
            when {
                // Found the smallest so far
                num <= first -> first = num
                // Found something bigger than first, but smaller than second
                num <= second -> second = num
                // Found something bigger than both first and second!
                else -> return true
            }
        }

        return false
    }
}
