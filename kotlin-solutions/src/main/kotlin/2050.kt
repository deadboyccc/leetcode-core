package two050;

class Solution {
    fun minimumTime(n: Int, relations: Array<IntArray>, time: IntArray): Int {
        val graph = Array(n + 1) { mutableListOf<Int>() }
        val inDegrees = IntArray(n + 1) { 0 }
        relations.forEach { (prev, next) ->
            graph[prev].add(next)
            inDegrees[next]++
        }

        // dp[i] = earliest month course i finishes
        val dp = IntArray(n + 1) { 0 }

        val queue = ArrayDeque<Int>()
        (1..n).forEach { course ->
            if (inDegrees[course] == 0) {
                dp[course] = time[course - 1]
                queue.addLast(course)
            }
        }

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            graph[node].forEach { neighbor ->
                // neighbor finishes at: when its slowest prereq finishes + its own duration
                dp[neighbor] = maxOf(dp[neighbor], dp[node] + time[neighbor - 1])
                if (--inDegrees[neighbor] == 0) queue.addLast(neighbor)
            }
        }

        return dp.max()
    }
}