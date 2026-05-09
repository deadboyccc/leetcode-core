package nine76

class MoreReadableSolution {
    fun largestPerimeter(nums: IntArray): Int {
        // Step 1: Sort descending so we find the LARGEST perimeter first
        nums.sortDescending()

        // Step 2: Iterate through the array.
        // Each 'nums[i]' is your 'candidateBigSide'
        for (i in 0 until nums.size - 2) {
            val candidateBigSide = nums[i]

            // Your intuition fix: The "Best Supporting Sides" are
            // always the ones right next to it in a sorted list.
            val sideB = nums[i + 1]
            val sideC = nums[i + 2]

            // The Triangle Rule: Sum of smaller sides > Big side
            if (sideB + sideC > candidateBigSide) {
                return candidateBigSide + sideB + sideC
            }
        }

        // If we finish the loop without finding one, return 0
        return 0
    }
}

class Solution {
    fun largestPerimeter(nums: IntArray): Int {
        // Sort descending to find the largest values first
        nums.sortDescending()

        // We check triplets of adjacent numbers: nums[i], nums[i+1], nums[i+2]
        for (i in 0 until nums.size - 2) {
            val a = nums[i]
            val b = nums[i + 1]
            val c = nums[i + 2]

            // The Triangle Inequality: sum of two smaller sides > longest side
            if (b + c > a) {
                return a + b + c
            }
        }
        return 0
    }
}
