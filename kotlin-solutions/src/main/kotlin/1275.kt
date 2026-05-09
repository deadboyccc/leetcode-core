package one275

class Optimized {
    class Solution {
        fun tictactoe(moves: Array<IntArray>): String {
            // Since it's always 3x3, arrays are faster than maps
            val rows = IntArray(3)
            val cols = IntArray(3)
            var negDiag = 0 // r - c
            var posDiag = 0 // r + c

            for (i in moves.indices) {
                val r = moves[i][0]
                val c = moves[i][1]

                // Player A uses 1, Player B uses -1
                val value = if (i % 2 == 0) 1 else -1

                // Update trackers
                rows[r] += value
                cols[c] += value
                if (r == c) negDiag += value
                if (r + c == 2) posDiag += value

                // Check if current player reached 3 (A) or -3 (B)
                val winTarget = value * 3
                if (rows[r] == winTarget || cols[c] == winTarget ||
                    negDiag == winTarget || posDiag == winTarget
                ) {
                    return if (value == 1) "A" else "B"
                }
            }

            return if (moves.size == 9) "Draw" else "Pending"
        }
    }
}

// over engineering Galore
class Solution {
    val rowMap = mutableMapOf<String, Int>()
    val colMap = mutableMapOf<String, Int>()
    val negDiagMap = mutableMapOf<String, Int>()
    val posDiagMap = mutableMapOf<String, Int>()

    fun tictactoe(moves: Array<IntArray>): String {
        rowMap.clear(); colMap.clear(); negDiagMap.clear(); posDiagMap.clear()

        for (i in moves.indices) {
            val (r, c) = moves[i]
            val char = if (i % 2 == 0) 'A' else 'B'
            if (occupyAndCheck(r, c, char)) return char.toString()
        }

        return if (moves.size == 9) "Draw" else "Pending"
    }

    private fun occupyAndCheck(r: Int, c: Int, char: Char): Boolean {
        val p = char.toString()

        val rCount = rowMap.merge("$p-r$r", 1, Int::plus) ?: 1
        val cCount = colMap.merge("$p-c$c", 1, Int::plus) ?: 1
        val negCount = if (r == c) negDiagMap.merge("$p-neg", 1, Int::plus) ?: 1 else 0
        val posCount = if (r + c == 2) posDiagMap.merge("$p-pos", 1, Int::plus) ?: 1 else 0

        return rCount == 3 || cCount == 3 || negCount == 3 || posCount == 3
    }
}
