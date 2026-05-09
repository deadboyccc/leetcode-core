package two83

// too much brute force
class Solution {
    fun moveZeroes(nums: IntArray): Unit {
        if (nums[0] == 0 && nums.size == 1) return
        val arr = mutableListOf<Int>()
        var zeroCount = 0
        nums.forEach {
            if (it != 0) {
                arr.add(it)
                return@forEach
            }
            zeroCount++
        }
        (0..zeroCount).forEach { arr.add(0) }


        for (i in 0 until nums.size) {
            nums[i] = arr[i]
        }
    }
}


// at cost of mem
class ImprovedSolution {
    fun moveZeroes(nums: IntArray): Unit {
        val zeroCount = nums.count { it == 0 }
        if (zeroCount == 0) return

        val arr = (nums.filter { it != 0 } + IntArray(zeroCount) { 0 }.toList()).toIntArray()
        arr.copyInto(nums)
    }
}

// most optimal tp solution
class SolutionBrute {
    fun moveZeroes(nums: IntArray): Unit {
        var insert = 0
        for (i in nums.indices) {
            if (nums[i] != 0) nums[insert++] = nums[i]
        }
        while (insert < nums.size) nums[insert++] = 0
    }
}

fun main() {
    Solution().moveZeroes(intArrayOf(8, 10, 0, 20, 0, 50, 0))
}