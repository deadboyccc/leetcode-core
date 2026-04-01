package seven04;

class Solution {
    fun search(nums: IntArray, target: Int): Int {
        if (nums.isEmpty()) return 0
        return recursiveBns(nums, target, 0, nums.lastIndex)


    }

    fun recursiveBns(nums: IntArray, target: Int, left: Int, right: Int): Int {
        if (left > right) return -1

        val mid = left + (right - left) / 2

        return when {
            nums[mid] == target -> mid
            nums[mid] > target -> recursiveBns(nums, target, left, mid - 1)
            else -> recursiveBns(nums, target, mid + 1, right)
        }
    }
}