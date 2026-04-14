package one822;

/*
1 if x is positive.
-1 if x is negative.
0 if x is equal to 0.
 */
class Solution {
    fun arraySign(nums: IntArray): Int {
        var sign = 1
        for (n in nums) {
            when {
                n == 0 -> return 0 // Early exit if we hit a zero
                n < 0 -> sign *= -1 // Flip the sign
            }
        }
        return sign
    }
}

class SolutionFP {
    fun arraySign(nums: IntArray): Int {
        return nums.fold(1) { acc, i ->
            // If acc or i is 0, the result stays 0
            // Otherwise, we only multiply the directions (-1 or 1)
            acc * when {
                i > 0 -> 1
                i < 0 -> -1
                else -> 0
            }
        }
    }
}
