package six82

class Solution {
    fun calPoints(operations: Array<String>): Int {
        val history = mutableListOf<Int>()

        for (op in operations) {
            when (op) {
                "+" -> {
                    // Take the last two and add their sum to history
                    val last = history[history.lastIndex]
                    val secondLast = history[history.lastIndex - 1]
                    history.add(last + secondLast)
                }

                "D" -> {
                    // Double the last score
                    history.add(history.last() * 2)
                }

                "C" -> {
                    // Invalidate (remove) the last score
                    history.removeAt(history.lastIndex)
                }

                else -> {
                    // It's an integer (can be negative, so toInt() is safer than isDigit check)
                    history.add(op.toInt())
                }
            }
        }

        return history.sum()
    }
}
