package five47

class Solution {

    fun findCircleNum(isConnected: Array<IntArray>): Int {
        val visited = BooleanArray(isConnected.size)
        var provinces = 0

        fun dfs(city: Int) {
            visited[city] = true

            for (neighbor in isConnected.indices) {
                val isNeighbor = isConnected[city][neighbor] == 1

                if (isNeighbor && !visited[neighbor]) {
                    dfs(neighbor)
                }
            }
        }

        for (city in isConnected.indices) {
            if (!visited[city]) {
                dfs(city)
                provinces++
            }
        }

        return provinces
    }
}
