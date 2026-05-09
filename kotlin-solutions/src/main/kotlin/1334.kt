package one334;

class Solution {
    private val INF = Long.MAX_VALUE / 2

    fun findTheCity(n: Int, edges: Array<IntArray>, distanceThreshold: Int): Int {
        val dist = edges
            .flatMap { (u, v, w) ->
                listOf(Triple(u, v, w.toLong()), Triple(v, u, w.toLong()))
            }
            .let { buildMatrix(n, it) }
            .let { floydWarshall(it) }

        return (n - 1 downTo 0).minByOrNull { city ->
            dist[city].count { d -> d in 1..distanceThreshold }
        } ?: (n - 1)
    }

    private fun buildMatrix(n: Int, edges: List<Triple<Int, Int, Long>>): Array<LongArray> {
        val best = edges
            .groupBy({ it.first to it.second }, { it.third })
            .mapValues { (_, ws) -> ws.min() }

        return Array(n) { i ->
            LongArray(n) { j -> if (i == j) 0L else best[i to j] ?: INF }
        }
    }

    private fun floydWarshall(w: Array<LongArray>): Array<LongArray> =
        w.indices.fold(w) { cur, k ->
            Array(cur.size) { i ->
                LongArray(cur.size) { j ->
                    if (cur[i][k] < INF && cur[k][j] < INF) minOf(cur[i][j], cur[i][k] + cur[k][j])
                    else cur[i][j]
                }
            }
        }
}
