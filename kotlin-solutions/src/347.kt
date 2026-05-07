package three47

import java.util.*

// --- Solution 1: Sorting ---
// Time Complexity: O(n + m log m) where n is number of elements and m is unique elements.
// Space Complexity: O(m) to store the frequency map.
class Solution {
    fun topKFrequent(nums: IntArray, k: Int): IntArray {
        // num -> freq
        var freq = hashMapOf<Int, Int>()
        for (num in nums) {
            freq[num] = freq.getOrDefault(num, 0) + 1
        }
        return freq.keys.sortedByDescending { freq[it] }.take(k).toIntArray()
    }
}

// --- Solution 2: Min-Heap (Priority Queue) ---
// Time Complexity: O(n log k) as we maintain a heap of size k.
// Space Complexity: O(m + k) for the frequency map and the heap.
class Solution2 {
    fun topKFrequent(nums: IntArray, k: Int): IntArray {
        // groupingBy + eachCount is the idiomatic way to build a frequency map
        val freqMap = nums.toList().groupingBy { it }.eachCount()

        // Use a PriorityQueue to keep track of the top K elements
        val minHeap = PriorityQueue<Int>(compareBy { freqMap[it] })

        freqMap.keys.forEach { num ->
            minHeap.add(num)
            if (minHeap.size > k) minHeap.poll()
        }

        // Convert the final heap state directly to the result array
        return IntArray(k) { minHeap.poll() }
    }
}

// --- Solution 3: Bucket Sort ---
// Time Complexity: O(n) as we iterate through the array and buckets linearly.
// Space Complexity: O(n) to store the frequency map and the bucket array.
class BucketSortSolution {
    class Solution {
        fun topKFrequent(nums: IntArray, k: Int): IntArray {
            // 1. Map numbers to their frequencies: O(n)
            val freqMap = nums.toList().groupingBy { it }.eachCount()

            // 2. Create buckets where index = frequency: O(n)
            val buckets = Array(nums.size + 1) { mutableListOf<Int>() }

            freqMap.forEach { (num, count) ->
                buckets[count].add(num)
            }

            // 3. Collect top k elements by iterating backwards: O(n)
            val result = IntArray(k)
            var index = 0

            for (f in buckets.size - 1 downTo 0) {
                for (num in buckets[f]) {
                    result[index++] = num
                    if (index == k) return result
                }
            }

            return result
        }
    }
}
