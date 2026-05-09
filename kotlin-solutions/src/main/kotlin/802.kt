package eight02

class Solution {
    fun eventualSafeNodes(graph: Array<IntArray>): List<Int> {

        // 0 = unvisited, 1 = on current path, 2 = proven safe
        val state = Array(graph.size) { 0 }
        val res = mutableListOf<Int>()

        // returns true if a cycle is reachable from curNode
        fun dfs(curNode: Int): Boolean {

            // we've looped back to a node already on our current path → cycle found
            if (state[curNode] == 1) return true

            // already proven safe in a previous DFS call → no need to re-explore
            if (state[curNode] == 2) return false

            // mark as "currently being explored" (on the call stack)
            state[curNode] = 1

            // visit every node this node points to
            graph[curNode].forEach { neighbor ->

                // if any neighbor leads to a cycle, this node is unsafe too → bubble up
                if (dfs(neighbor)) return true
            }

            // exhausted all neighbors with no cycle found → this node is safe
            state[curNode] = 2

            // report: no cycle reachable from here
            return false
        }

        graph.indices.forEach { node ->

            // dfs returns true if unsafe (has cycle), false if safe
            if (!dfs(node)) {
                // no cycle reachable → safe node → collect it
                res.add(node)
            }
        }

        // result is naturally sorted because we iterate indices 0..n-1 in order
        return res.toList()
    }
}
