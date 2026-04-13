package five3;

// greedy Kodaine 101

// [1,3,-7,10]

class Solution {
    fun maxSubArray(nums: IntArray): Int {

        var maxSum = nums[0]
        var currSum = 0

        for (num in nums) {
            // If currSum becomes negative, it's a "burden" -> reset it to 0
            if (currSum < 0) currSum = 0

            currSum += num
            maxSum = maxOf(maxSum, currSum)
        }

        return maxSum
    }
}
