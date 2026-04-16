package seven39

class Solution {
    fun dailyTemperatures(temperatures: IntArray): IntArray {
        val stack = ArrayDeque<Int>()
        val res = IntArray(temperatures.size)

        for (i in temperatures.indices.reversed()) {
            // 1. Pop indices that are not warmer than the current temperature
            while (stack.isNotEmpty() && temperatures[i] >= temperatures[stack.last()])
                stack.removeLast()


            // 2. If stack isn't empty, the top index is the next warmer day
            if (stack.isNotEmpty())
                res[i] = stack.last() - i


            // 3. Push current day index onto the stack
            stack.addLast(i)
        }
        return res
    }
}
