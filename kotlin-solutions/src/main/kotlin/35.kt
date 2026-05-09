package three5

class Optimized {
    class Solution {

        // nums is sorted in ascending order with distinct values
        fun searchInsert(nums: IntArray, target: Int): Int {
            for (i in nums.indices) {
                if (nums[i] >= target) {
                    return i
                }
            }

            return nums.size
        }
    }

}

class Solution {

    // nums contains distinct values sorted in ascending order
    fun searchInsert(nums: IntArray, target: Int): Int {
        var ptr = 0

        for (i in nums.indices) {
            when {
                nums[i] == target -> return i
                nums[i] < target -> ptr++
                nums[i] > target -> return ptr
            }
        }

        return ptr
    }
}
