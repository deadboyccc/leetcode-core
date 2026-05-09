package eight4


class SolutionSentinel {
    fun largestRectangleArea(heights: IntArray): Int {
        val stack = ArrayDeque<Pair<Int, Int>>()
        var maxArea = 0

        for ((i, h) in (heights.toList() + 0).withIndex()) {
            var start = i
            while (stack.isNotEmpty() && h < stack.last().second) {
                val (prevIndex, prevHeight) = stack.removeLast()
                maxArea = maxOf(maxArea, prevHeight * (i - prevIndex))
                start = prevIndex
            }
            stack.addLast(start to h)
        }

        return maxArea
    }
}

class Solution {
    fun largestRectangleArea(heights: IntArray): Int {

        val stack = ArrayDeque<Pair<Int, Int>>()  // index -> height
        var maxArea = 0

        for (i in heights.indices) {
            var start = i
            while (stack.isNotEmpty() && heights[i] < stack.last().second) {
                val (prevIndex, prevHeight) = stack.removeLast()
                maxArea = maxOf(maxArea, prevHeight * (i - prevIndex))
                start = prevIndex
            }
            stack.addLast(start to heights[i])
        }

        for ((index, height) in stack)
            maxArea = maxOf(maxArea, height * (heights.size - index))

        return maxArea
    }
}