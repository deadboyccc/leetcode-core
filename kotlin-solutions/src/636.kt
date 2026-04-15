package six36

class Solution {
    fun exclusiveTime(n: Int, logs: List<String>): IntArray {
        val result = IntArray(n)
        val stack = ArrayDeque<Int>()
        // prev represents the start of the current interval being processed
        var prev = 0

        logs.forEach { log ->
            val parts = log.split(":")
            val id = parts[0].toInt()
            val type = parts[1]
            val time = parts[2].toInt()

            if (type == "start") {
                // If there's a function already running, add the time it spent
                // from 'prev' until now (time - 1)
                if (stack.isNotEmpty()) {
                    result[stack.last()] += time - prev
                }
                stack.addLast(id)
                prev = time
            } else {
                // Function is ending. It ran from 'prev' to 'time' inclusive.
                // We use + 1 because the end time is inclusive.
                result[stack.removeLast()] += time - prev + 1
                // The next interval starts at the next unit of time
                prev = time + 1
            }
        }

        return result
    }
}
