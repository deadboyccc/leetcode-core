package one8

/**
 * 1. NAIVE APPROACH
 *
 * Idea:
 * Try every possible quadruplet using 4 nested loops.
 *
 * Time Complexity: O(N^4)
 * Space Complexity: O(1) auxiliary space
 */
class NaiveSolution {

    fun fourSum(nums: IntArray, target: Int): List<List<Int>> {
        if (nums.size < 4) return emptyList()

        val result = mutableSetOf<List<Int>>() // avoids duplicate quadruplets
        val n = nums.size

        // Sorting ensures duplicates appear in the same order
        nums.sort()

        for (i in 0 until n - 3) {
            for (j in i + 1 until n - 2) {
                for (k in j + 1 until n - 1) {
                    for (l in k + 1 until n) {

                        // Use Long to safely avoid integer overflow
                        val sum =
                            nums[i].toLong() +
                                    nums[j].toLong() +
                                    nums[k].toLong() +
                                    nums[l].toLong()

                        if (sum == target.toLong()) {
                            result.add(
                                listOf(
                                    nums[i],
                                    nums[j],
                                    nums[k],
                                    nums[l]
                                )
                            )
                        }
                    }
                }
            }
        }

        return result.toList()
    }
}

/**
 * 2. OPTIMAL APPROACH
 *
 * Idea:
 * - Sort the array
 * - Fix the first 2 numbers
 * - Use two pointers for the remaining 2 numbers
 *
 * Time Complexity: O(N^3)
 * Space Complexity: O(1) auxiliary space
 */
class OptimalSolution {

    fun fourSum(nums: IntArray, target: Int): List<List<Int>> {
        if (nums.size < 4) return emptyList()

        nums.sort()

        val result = mutableListOf<List<Int>>()
        val n = nums.size

        for (i in 0 until n - 3) {

            // Skip duplicate first values
            if (i > 0 && nums[i] == nums[i - 1]) continue

            for (j in i + 1 until n - 2) {

                // Skip duplicate second values
                if (j > i + 1 && nums[j] == nums[j - 1]) continue

                var left = j + 1
                var right = n - 1

                while (left < right) {

                    val sum =
                        nums[i].toLong() +
                                nums[j].toLong() +
                                nums[left].toLong() +
                                nums[right].toLong()

                    when {
                        sum == target.toLong() -> {

                            result.add(
                                listOf(
                                    nums[i],
                                    nums[j],
                                    nums[left],
                                    nums[right]
                                )
                            )

                            // Skip duplicate third values
                            while (
                                left < right &&
                                nums[left] == nums[left + 1]
                            ) {
                                left++
                            }

                            // Skip duplicate fourth values
                            while (
                                left < right &&
                                nums[right] == nums[right - 1]
                            ) {
                                right--
                            }

                            left++
                            right--
                        }

                        sum < target -> {
                            // Need a larger sum
                            left++
                        }

                        else -> {
                            // Need a smaller sum
                            right--
                        }
                    }
                }
            }
        }

        return result
    }
}
