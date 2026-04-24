package one559

class Solution {
    fun containsCycle(grid: Array<CharArray>): Boolean {
        val rows = grid.size
        val cols = grid[0].size
        val directions = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
        val visited = Array(rows) { BooleanArray(cols) { false } }

        fun dfs(row: Int, col: Int, parentRow: Int, parentCol: Int, ch: Char): Boolean {
            if (row !in 0 until rows || col !in 0 until cols) return false
            if (grid[row][col] != ch) return false
            if (visited[row][col]) return true   // reached visited same-char cell → cycle

            visited[row][col] = true

            for ((dr, dc) in directions) {
                val nr = row + dr
                val nc = col + dc
                if (nr == parentRow && nc == parentCol) continue  // skip parent
                if (dfs(nr, nc, row, col, ch)) return true
            }

            return false
        }

        for (i in grid.indices) {
            for (j in grid[0].indices) {
                if (!visited[i][j]) {
                    if (dfs(i, j, -1, -1, grid[i][j])) return true
                }
            }
        }

        return false
    }
}
