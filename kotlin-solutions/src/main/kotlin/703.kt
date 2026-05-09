package seven03

import java.util.*

/**
 * PROBLEM: Kth Largest Element in a Stream
 * Maintain a Min-Heap of size K. The root (smallest) of this heap
 * represents the Kth largest element of the entire stream.
 */

// ========================================================
// 1. MOST READABLE (INTERVIEW FRIENDLY)
// Logic: "Add first, then trim."
// ========================================================
class KthLargestReadable(private val k: Int, nums: IntArray) {
    private val heap = PriorityQueue<Int>()

    init {
        nums.forEach { add(it) }
    }

    fun add(`val`: Int): Int {
        heap.offer(`val`)           // Always add the value first

        if (heap.size > k) {
            heap.poll()             // If we exceed size K, remove the smallest
        }

        return heap.peek()          // The root is now the Kth largest
    }
}

// ========================================================
// 2. MOST OPTIMIZED (PRODUCTION STANDARD)
// Logic: "Only touch the heap if the value qualifies."
// ========================================================
class KthLargestOptimized(private val k: Int, nums: IntArray) {
    // Initializing capacity 'k' prevents internal array resizing
    private val heap = PriorityQueue<Int>(k)

    init {
        for (num in nums) process(num)
    }

    fun add(`val`: Int): Int {
        process(`val`)
        return heap.peek()
    }

    private fun process(value: Int) {
        if (heap.size < k) {
            heap.offer(value)       // Fill heap to capacity first

        } else if (value > (heap.peek() ?: Int.MIN_VALUE)) {
            heap.poll()             // Remove old boundary
            heap.offer(value)       // Insert new candidate
        }
    }
}

// ========================================================
// 3. FASTEST PATH (MINIMAL OVERHEAD)
// Logic: "Inlined hot-path with zero extra function calls."
// ========================================================
class KthLargestFast(private val k: Int, nums: IntArray) {
    private val heap = PriorityQueue<Int>(k)

    init {
        for (num in nums) {
            if (heap.size < k) {
                heap.offer(num)
            } else if (num > heap.peek()) {
                heap.poll()
                heap.offer(num)
            }
        }
    }

    fun add(`val`: Int): Int {
        // Minimal branching for JVM performance
        if (heap.size < k) {
            heap.offer(`val`)
        } else if (`val` > heap.peek()) {
            heap.poll()
            heap.offer(`val`)
        }
        return heap.peek()
    }
}

// fastest from LeetCode Beats 100%
class KthLargest(k: Int, nums: IntArray) {

    val heap = PriorityQueue<Int>()
    private val k: Int

    init {
        this.k = k
        for (i in nums) {
            add(i)
        }
    }

    fun add(`val`: Int): Int {
        heap.offer(`val`)

        if (heap.size > k) {
            heap.poll()
        }

        return heap.peek()
    }


}


