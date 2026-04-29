package five5

class Solution {
    // top down recursive memo
    fun canJump(nums: IntArray): Boolean {
        var goal = nums.size - 1
        for (i in nums.lastIndex - 1 downTo 0) {
            if (nums[i] >= goal) goal = i


        }
        return if (goal == 0) false else true


    }
}