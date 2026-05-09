package two07

class Solution {
    fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        // one empty neighbor list per course, indexed by course number
        val adjacencyList = Array(numCourses) { mutableListOf<Int>() }

        // draw directed edge: prereq → course ("take prereq before course")
        prerequisites.forEach { (course, prereq) -> adjacencyList[prereq].add(course) }

        // can finish all courses only if there is no circular dependency
        return !hasCycleDfs(adjacencyList, numCourses)
    }

    private fun hasCycleDfs(
        adjacencyList: Array<MutableList<Int>>,
        nodeCount: Int
    ): Boolean {
        // globally tracks nodes we have fully finished exploring
        val visited = BooleanArray(nodeCount)

        // tracks nodes currently on the active dfs call stack
        // this is what actually catches cycles, not visited
        val onCurrentPath = BooleanArray(nodeCount)

        // try every node as a starting point to handle disconnected parts of the graph
        // short-circuits immediately if any starting point leads to a cycle
        // we need one True
        return adjacencyList.indices.any { node ->
            !visited[node] && dfsCycleCheck(node, adjacencyList, visited, onCurrentPath)
        }
    }

    private fun dfsCycleCheck(
        node: Int,
        adjacencyList: Array<MutableList<Int>>,
        visited: BooleanArray,
        onCurrentPath: BooleanArray
    ): Boolean {
        // mark this node as seen so we never re-enter it from another path
        visited[node] = true

        // stamp this node onto the active path so we can detect if we loop back to it
        onCurrentPath[node] = true

        val cycleFound = adjacencyList[node].any { neighbor ->
            // neighbor is already on our current path — we just found a back-edge, cycle confirmed
            onCurrentPath[neighbor] ||
                    // neighbor is fresh — go deeper and check if it eventually loops back
                    (!visited[neighbor] && dfsCycleCheck(neighbor, adjacencyList, visited, onCurrentPath))
        }

        // leaving this node — remove it from the active path so it doesn't
        // block other independent dfs branches from visiting it legitimately
        onCurrentPath[node] = false

        return cycleFound
    }
}
