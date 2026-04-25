package one69

class Solution {
    fun majorityElement(nums: IntArray): Int =
        nums.toList().groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: 0
    // time o(n) , space o(1)
}

class OptimalSolution {
    fun majorityElement(nums: IntArray): Int {
        var count = 0
        var candidate = 0

        for (num in nums) {
            // If count is 0, we pick the current number as the new candidate
            if (count == 0) {
                candidate = num
            }

            // If the current number matches the candidate, increment; else decrement
            count += if (num == candidate) 1 else -1
        }

        return candidate
    }
}
