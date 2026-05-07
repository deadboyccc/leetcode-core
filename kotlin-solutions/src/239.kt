package two39

import misc.kia_part_one.printSeperator

class SolutionCleanButCostly {
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
//    println(Solution().maxSlidingWindow(intArrayOf(1, 3, -1, -3, 5, 3, 6, 7), 3).contentToString())

    //The Best Test Case: [5, 2, 4, 3, 1]
    printSeperator()
    val testArray = intArrayOf(5, 2, 4, 3, 1)
    IncreasingMonotonicStack(testArray)
}

class IncreasingMonotonicStack(val nums: IntArray) {
    private val stack = ArrayDeque<Int>()

    init {
        for (num in nums) {
            addIncreasing(num)
        }
    }

    private fun addIncreasing(num: Int) {
        println("--- Processing: $num ---")
        printStack("Before      ")

        while (stack.isNotEmpty() && stack.last() > num) {
            val popped = stack.removeLast()
            println("Intermediate: Popped $popped | Current Stack: $stack")
        }

        stack.addLast(num)
        printStack("After       ")
        println() // Extra line for readability between numbers
    }

    private fun printStack(prefix: String) {
        println("$prefix: $stack")
    }
}

class IncreasingMonotonicStackFancyPrint(val nums: IntArray) {
    private val stack = ArrayDeque<Int>()

    init {
        println("%-10s | %-10s | %-20s | %s".format("Input", "Phase", "Stack State", "Action"))
        println("-".repeat(60))
        for (num in nums) {
            process(num)
        }
    }

    private fun process(num: Int) {
        // Initial state for this specific number
        printRow(num, "Start", stack.toString(), "Processing $num")

        while (stack.isNotEmpty() && stack.last() > num) {
            val popped = stack.removeLast()
            printRow(num, "Pop", stack.toString(), "Popped $popped (>$num)")
        }

        stack.addLast(num)
        printRow(num, "Push", stack.toString(), "Added $num")
        println("-".repeat(60)) // Separator between numbers
    }

    private fun printRow(input: Int, phase: String, state: String, action: String) {
        println("%-10d | %-10s | %-20s | %s".format(input, phase, state, action))
    }
}

