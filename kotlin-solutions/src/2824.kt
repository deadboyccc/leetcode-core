package two824

class MostReadableSolution {

    fun countPairs(nums: List<Int>, target: Int): Int {
        val sorted = nums.sorted()

        var count = 0
        var left = 0
        var right = sorted.lastIndex

        while (left < right) {
            if (sorted[left] + sorted[right] < target) {
                // every value between left+1..right also works
                count += right - left
                left++
            } else {
                right--
            }
        }

        return count
    }
}
class FPSolution {
    fun countPairs(nums: List<Int>, target: Int): Int =

        nums.indices.fold(0) { count, i ->
            count + nums.subList(i + 1, nums.size)
                .count { nums[i] + it < target }
        }

}

class Solution {
    fun countPairs(nums: List<Int>, target: Int): Int {
        val sorted = nums.sorted()

        tailrec fun solve(
            left: Int,
            right: Int,
            count: Int
        ): Int {
            if (left >= right) return count

            return if (sorted[left] + sorted[right] < target) {
                // all elements between left..right form valid pairs
                solve(left + 1, right, count + (right - left))
            } else {
                solve(left, right - 1, count)
            }
        }

        return solve(0, sorted.lastIndex, 0)
    }
}

class CleanSolution {
    fun countPairs(nums: List<Int>, target: Int): Int {
        val sorted = nums.sorted()

        var left = 0
        var right = sorted.lastIndex
        var count = 0

        while (left < right) {
            val sum = sorted[left] + sorted[right]

            if (sum < target) {
                /*
                 If sorted[left] + sorted[right] < target,
                 then every element between left+1..right
                 also forms a valid pair with left
                 because the array is sorted.

                 So we add:
                 (right - left)
                */
                count += right - left
                left++
            } else {
                // sum too large -> decrease it
                right--
            }
        }

        return count
    }
}