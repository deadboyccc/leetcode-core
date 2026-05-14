package six84

class MoreReadableIdiomatic {
    class Solution {
        fun findRedundantConnection(edges: Array<IntArray>): IntArray {
            val parent = IntArray(edges.size + 1) { it }

            fun find(x: Int): Int {
                if (parent[x] != x) parent[x] = find(parent[x])
                return parent[x]
            }

            for ((u, v) in edges) {
                val ru = find(u)
                val rv = find(v)
                if (ru == rv) return intArrayOf(u, v)
                parent[ru] = rv
            }

            error("unreachable")
        }
    }
}

class Solution {
    fun findRedundantConnection(edges: Array<IntArray>): IntArray {

        val n = edges.maxOf { it.max() } + 1
        val uf = UnionFind(n)

        for ((u, v) in edges) {
            if (!uf.union(u, v)) {
                return intArrayOf(u, v)
            }
        }

        return intArrayOf(-1, -1) // unreachable given valid input [ guaranteed ]
    }

    class UnionFind(n: Int) {
        private val parent = IntArray(n) { it }
        private val rank = IntArray(n) { 0 }

        fun find(x: Int): Int {
            if (parent[x] != x)
                parent[x] = find(parent[x])
            return parent[x]
        }

        fun union(x: Int, y: Int): Boolean {
            val rx = find(x)
            val ry = find(y)
            if (rx == ry) return false

            when {
                rank[rx] < rank[ry] -> parent[rx] = ry
                rank[rx] > rank[ry] -> parent[ry] = rx
                else -> {
                    parent[ry] = rx; rank[rx]++
                }
            }
            return true
        }

        fun connected(x: Int, y: Int) = find(x) == find(y)
        fun componentCount() = parent.indices.count { find(it) == it }
    }
}
