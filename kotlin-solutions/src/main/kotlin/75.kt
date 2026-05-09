package seven5;

class Solution {
    fun sortColors(nums: IntArray): Unit {

        // We use asIterable() to gain access to groupingBy without copying the array
        val counts = nums.asIterable().groupingBy { it }.eachCount()

        // fold handles the 'state' of our array index pointer functionally
        // acc           0,1,2
        (0..2).fold(0) { startIndex, color ->
            val count = counts[color] ?: 0
            val endIndex = startIndex + count

            nums.fill(color, startIndex, endIndex)

            endIndex // Return as next startIndex ( acc)
        }
    }
}
