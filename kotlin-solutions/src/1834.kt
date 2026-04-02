package one834;

import java.util.*

/**
 * PROBLEM: 1834. Single-Threaded CPU
 * * GOAL:
 * Return the order (indices) in which a single-threaded CPU processes n tasks.
 * * RULES:
 * 1. CPU can only process ONE task at a time.
 * 2. If idle and no tasks are available, wait until the next task arrives.
 * 3. If multiple tasks are available, the CPU picks based on priority:
 * - Priority A: Shortest processing time.
 * - Priority B (Tie-breaker): Smallest original index.
 * 4. Once started, a task must be completed without interruption.
 * * LOGIC / ALGORITHM:
 * - Attach original indices to tasks and sort them by Enqueue Time.
 * - Use a Min-Heap (Priority Queue) to store "available" tasks.
 * - Track 'currentTime'; move tasks from the sorted list to the Heap
 * as their Enqueue Time <= currentTime.
 * - If Heap is empty but tasks remain, jump 'currentTime' to the next
 * available task's arrival time.
 */
// draft
class Solution {
    // tasks = [arrivalTime, ProcessingTime, originalIndex]
    data class Task(val arrivalTime: Int, val processingTime: Int, val originalIndex: Int)

    fun getOrder(tasks: Array<IntArray>): IntArray {
        // sort tasks by arrivalTime - we start at t=1
        val tasks =
            tasks.mapIndexed { index, (arrivalTime, processingTime) ->
                Task(arrivalTime, processingTime, index)
            }
        tasks.sortedBy { it.processingTime }.sortedBy { it.originalIndex }

        // heap tasks by processing time then by index
        val pq = PriorityQueue<Task>(compareBy<Task> { it.processingTime }.thenBy { it.originalIndex })


        return intArrayOf();


    }


}