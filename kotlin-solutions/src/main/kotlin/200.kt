package two00;

class Solution {
    fun numIslands(grid: Array<CharArray>): Int {

        val visited = hashSetOf<Pair<Int, Int>>()
        var count = 0
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == '1' && !visited.contains(Pair(row, col))) {
                    dfs(grid, Pair(row, col), visited)
                    count++
                }

            }
        }
        return count


    }

    fun dfs(grid: Array<CharArray>, pair: Pair<Int, Int>, visited: HashSet<Pair<Int, Int>>) {
        val row = pair.first
        val col = pair.second

        visited.add(pair)

        val directions = arrayOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)
        )

        for (dir in directions) {
            val newRow = row + dir.first
            val newCol = col + dir.second
            val nextPair = Pair(newRow, newCol)

            if (newRow in grid.indices &&
                newCol in grid[0].indices &&
                grid[newRow][newCol] == '1' &&
                !visited.contains(nextPair)
            ) {

                dfs(grid, nextPair, visited)
            }
        }
    }
}
