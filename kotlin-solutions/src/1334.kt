package one334

class Solution {
    private val INF = Long.MAX_VALUE / 2
    fun findTheCity(n: Int, edges: Array<IntArray>, distanceThreshold: Int): Int {

        val dist = IntArray(n)
        // todo


    }

    fun buildMatrix(n: Int, edges: List<Triple<Int, Int, Long>>): Array<LongArray> {
        val w = Array(n) { i -> LongArray(n) { j -> if (i == j) 0L else INF } }
        for ((u, v, wt) in edges) {
            w[u][v] = minOf(w[u][v], wt)
        }
        return w
    }
}
