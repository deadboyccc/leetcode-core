package two00;

class ConnectedComponents {
    class Solution {
        fun numIslands(grid: Array<CharArray>): Int {
            if (grid.isEmpty()) return 0

            val rows = grid.indices
            val cols = grid[0].indices
            val visited = mutableSetOf<Pair<Int, Int>>()
            var islandCount = 0

            // Idiomatic grid traversal
            for (r in rows) {
                for (c in cols) {
                    // If we find land and haven't been here, it's a new island
                    if (grid[r][c] == '1' && (r to c) !in visited) {
                        islandCount++
                        exploreIsland(grid, r, c, visited)
                    }
                }
            }
            return islandCount
        }

        private fun exploreIsland(
            grid: Array<CharArray>,
            startR: Int,
            startC: Int,
            visited: MutableSet<Pair<Int, Int>>
        ) {
            val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
            val queue = ArrayDeque<Pair<Int, Int>>().apply { add(startR to startC) }
            visited.add(startR to startC)

            while (queue.isNotEmpty()) {
                val (currR, currC) = queue.removeFirst()

                for ((dr, dc) in directions) {
                    val nr = currR + dr
                    val nc = currC + dc

                    // Idiomatic boundary and condition check
                    if (nr in grid.indices &&
                        nc in grid[0].indices &&
                        grid[nr][nc] == '1' &&
                        (nr to nc) !in visited
                    ) {
                        visited.add(nr to nc)
                        queue.add(nr to nc)
                    }
                }
            }
        }
    }
}

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
