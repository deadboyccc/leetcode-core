package two10

class Solution {
    fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
        val graph = Array(numCourses) { mutableListOf<Int>() }
        for ((course, prereq) in prerequisites) {
            graph[course].add(prereq)
        }

        val state = IntArray(numCourses) // 0=unvisited 1=visiting 2=done
        val result = mutableListOf<Int>()

        // true = cycle detected
        fun dfs(node: Int): Boolean {
            if (state[node] == 1) return true  // back-edge → cycle
            if (state[node] == 2) return false // already resolved, no cycle

            state[node] = 1
            if (graph[node].any { dfs(it) }) return true // propagate cycle

            state[node] = 2
            result.add(node)
            return false // no cycle in this subtree
        }

        if ((0 until numCourses).any { state[it] == 0 && dfs(it) }) return intArrayOf()

        return result.toIntArray()
    }
}
