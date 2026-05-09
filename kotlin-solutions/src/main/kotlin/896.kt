package eight96;

class Solution {
    fun isMonotonic(nums: IntArray): Boolean {
        // Check if it is non-decreasing OR non-increasing
        return (0 until nums.lastIndex).all { i -> nums[i] <= nums[i + 1] }
                ||
                (0 until nums.lastIndex).all { i -> nums[i] >= nums[i + 1] }
    }
}
