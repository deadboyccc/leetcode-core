package leetcode1971;

import java.util.*

class Solution {

    fun validPath(n: Int, edges: Array<IntArray>, source: Int, destination: Int): Boolean {
        val adj = mutableMapOf<Int, MutableList<Int>>()

        edges.forEach { (x, y) ->
            adj.getOrPut(x) { mutableListOf() }.add(y)
            adj.getOrPut(y) { mutableListOf() }.add(x)
        }
        return canReach(source, destination, adj)


    }

    fun canReach(
        start: Int,
        destination: Int,
        adj: Map<Int, List<Int>>
    ): Boolean {
        if (start == destination) return true

        val queue = ArrayDeque<Int>()
        val visited = mutableSetOf<Int>()

        queue.addLast(start)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            val neighbors = adj[current] ?: emptyList()

            for (neighbor in neighbors) {
                if (neighbor == destination) return true
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.addLast(neighbor)
                }
            }
        }

        return false
    }

}