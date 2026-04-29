package two13;

class Solution {
    fun rob(nums: IntArray): Int {
        // Edge case: If there's only one house, just rob it!
        if (nums.size == 1) return nums[0]

        // Helper function to solve the linear version
        fun robLinear(houses: List<Int>): Int {
            return houses.fold(0 to 0) { (prev2, prev1), n ->
                prev1 to maxOf(prev1, prev2 + n)
            }.second
        }

        // Return the MAX of the two possible circular constraints
        return maxOf(
            robLinear(nums.dropLast(1)),
            robLinear(nums.drop(1))
        )
    }
}
