const val INF = Long.MAX_VALUE / 2

fun floydWarshall(initialW: Array<LongArray>): Array<LongArray> =
    initialW.indices.fold(initialW) { w, k ->
        Array(w.size) { i ->
            LongArray(w.size) { j ->
                if (w[i][k] < INF && w[k][j] < INF) minOf(w[i][j], w[i][k] + w[k][j])
                else w[i][j]
            }
        }
    }

fun buildMatrix(n: Int, edges: List<Triple<Int, Int, Long>>): Array<LongArray> {
    val best = edges
        .groupBy({ it.first to it.second }, { it.third })
        .mapValues { (_, ws) -> ws.min() }

    return Array(n) { i ->
        LongArray(n) { j -> if (i == j) 0L else best[i to j] ?: INF }
    }
}

fun hasNegativeCycle(dist: Array<LongArray>): Boolean =
    dist.indices.any { i -> dist[i][i] < 0L }
