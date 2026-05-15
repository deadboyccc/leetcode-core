package one5;

class Solution {
    fun threeSum(nums: IntArray): List<List<Int>> {
        var left = 0
        val sorted = nums.sorted()
        val res = hashSetOf<List<Int>>()
        while (left <= sorted.lastIndex) {
            // fix a curNum
            val curNum = sorted[left]

            // basic 2sum -> 2 ptrs on sorted arr
            var low = left + 1
            var high = sorted.lastIndex
            while (low < high) {
                val lowNum = sorted[low]
                val highNum = sorted[high]
                val sum = lowNum + highNum + curNum
                if (sum == 0) res.add(listOf(curNum, lowNum, highNum))

                // update inner while states
                if (sum > 0) high-- else low++

            }

            //update state
            left++
        }
        return res.toList()
    }
}