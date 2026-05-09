package three264

import java.util.*


class SolutionOptimized {
    fun getFinalState(nums: IntArray, k: Int, multiplier: Int): IntArray {
        // Define the min-heap with a clear, idiomatic comparator
        // We compare by value, then by index for the tie-breaker
        val minHeap = PriorityQueue<Pair<Int, Int>>(
            compareBy({ it.first }, { it.second })
        )

        // Populate heap using withIndex() for idiomatic iteration
        nums.forEachIndexed { index, value ->
            minHeap.offer(value to index)
        }

        repeat(k) {
            val (value, index) = minHeap.poll()

            // Calculate new value and update original array
            val newValue = value * multiplier
            nums[index] = newValue

            // Push updated state back to heap
            minHeap.offer(newValue to index)
        }

        return nums
    }
}

class Solution {
    fun getFinalState(nums: IntArray, k: Int, multiplier: Int): IntArray {
        repeat(k) {
            // Finds the first index where the value is the minimum
            val firstMinIndex = nums.indices.minBy { nums[it] }
            nums[firstMinIndex] *= multiplier
        }

        return nums
    }
}