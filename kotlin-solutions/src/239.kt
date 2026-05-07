package two39

class SolutionCleanButCosty {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray = nums.toList()
        .windowed(k)
        .map { it.max() }.toIntArray()
}

class Solution {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        val result = IntArray(nums.size - k + 1)
        val deque = ArrayDeque<Int>() // stores indices

        for (i in nums.indices) {
            // remove indices that have fallen outside the window
            if (deque.isNotEmpty() && deque.first() <= i - k)
                deque.removeFirst()

            // maintain decreasing invariant: drop indices whose values
            // are <= current, since they can never be the window max
            while (deque.isNotEmpty() && nums[deque.last()] <= nums[i])
                deque.removeLast()

            deque.addLast(i)

            // front of deque is always the index of the window max
            if (i >= k - 1)
                result[i - k + 1] = nums[deque.first()]
        }

        return result
    }
}

fun main() {
    //[1,3,-1,-3,5,3,6,7]
    println(Solution().maxSlidingWindow(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3).contentToString())

}

