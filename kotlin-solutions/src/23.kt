package two3

import java.util.*

class ListNode(var `val`: Int, var next: ListNode? = null)

class Solution {
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        // Use a dummy node to simplify head management
        // (Dummy)->null
        // (tail)<==>(dummy)
        // tail.next -> smallest  || dummy.next is the start of the list
        // tail is the iterator  || after assigning its next to smallest it becomes smallest by ref swap
        val dummy = ListNode(0)
        var tail: ListNode? = dummy

        // Min-heap to keep track of the smallest current node across all lists
        // Kotlin's compareBy is concise and efficient here
        val minHeap = PriorityQueue<ListNode>(compareBy { it.`val` })

        // Initial populate: only add non-null heads
        lists.forEach { list ->
            list?.let { minHeap.add(it) }
        }

        while (minHeap.isNotEmpty()) {
            // Get the smallest node and attach it to our result list
            val smallest = minHeap.poll()
            tail?.next = smallest
            tail = tail?.next

            // If the popped node has a successor, add it to the heap
            smallest.next?.let { minHeap.add(it) }
        }

        // Return the node following the dummy placeholder
        return dummy.next
    }
}
