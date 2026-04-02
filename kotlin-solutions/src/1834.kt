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
    // tasks = [arrivalTime, ProcessingTime]
    data class Task(val arrivalTime: Int, val processingTime: Int, val index: Int)

    fun getOrder(tasks: Array<IntArray>): IntArray {
        // sort tasks by arrivalTime - we start at t=1
        tasks.sortBy { it[0] }

        // heap tasks by processing time then by index
        val pq = PriorityQueue<Task>(compareBy<Task> { it.processingTime }.thenBy { it.index })

        // answers
        val sequenceOfTasks = mutableListOf<Int>()
        var currTime = 1;
        var taskIndex = 0;

        // answers < tasks
        while (sequenceOfTasks.size < tasks.size) {

            for (i in taskIndex until tasks.size) {
                if (tasks[i][0] > currTime) {
                    break
                }
                pq.add(Task(i, currTime, currTime))
            }
            while (pq.isNotEmpty()) {
                val currTask = pq.poll()
                currTime += currTask.processingTime
                sequenceOfTasks.add(currTask.index)
            }

        }
        return sequenceOfTasks.toIntArray()


    }


}