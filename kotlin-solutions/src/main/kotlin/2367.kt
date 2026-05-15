package two367

class Solution {
    fun arithmeticTriplets(nums: IntArray, diff: Int): Int {
        val numSet = nums.toHashSet()
        var count = 0

        for (num in nums) {
            // If both required future values exist, we found a triplet
            // Future values -> arr is sorted
            if (numSet.contains(num + diff) && numSet.contains(num + 2 * diff)) {
                count++
            }
        }

        return count
    }
}
