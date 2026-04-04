package fifty1;

class Solution {
    fun solveNQueens(n: Int): List<List<String>> {
        val col = mutableSetOf<Int>()
        val posDiag = mutableSetOf<Int>()
        val negDiag = mutableSetOf<Int>()

        val res = mutableListOf<List<String>>()
        val board = Array(n) { CharArray(n) { '.' } }

        fun backtrack(r: Int) {
            if (r == n) {
                res.add(board.map { it.joinToString("") })
                return
            }

            for (c in 0 until n) {
                if (c in col || (r + c) in posDiag || (r - c) in negDiag) {
                    continue
                }

                // Add Queen
                col.add(c)
                posDiag.add(r + c)
                negDiag.add(r - c)
                board[r][c] = 'Q'

                // Backtrack
                backtrack(r + 1)

                // Remove Queen
                col.remove(c)
                posDiag.remove(r + c)
                negDiag.remove(r - c)
                board[r][c] = '.'
            }
        }

        backtrack(0)
        return res
    }


}

fun main() {
}
