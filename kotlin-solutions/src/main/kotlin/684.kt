package six84

// ─────────────────────────────────────────────────────────────
// APPROACH 1 — Lean / idiomatic
// No rank, no class. Works because n ≤ 1000 and path compression
// alone is fast enough.
// ─────────────────────────────────────────────────────────────
class MoreReadableIdiomatic {
    class Solution {
        fun findRedundantConnection(edges: Array<IntArray>): IntArray {

            // Each node is its own parent initially (n isolated nodes)
            val parent = IntArray(edges.size + 1) { it }

            // Walk up to root, flattening the path on the way back (path compression)
            fun find(x: Int): Int {
                if (parent[x] != x) parent[x] = find(parent[x])
                return parent[x]
            }

            for ((u, v) in edges) {
                val ru = find(u)
                val rv = find(v)
                // Same root → adding this edge creates a cycle → it's the answer
                if (ru == rv) return intArrayOf(u, v)
                // Different roots → merge the two components
                parent[ru] = rv
            }

            error("unreachable: valid input always has exactly one redundant edge")
        }
    }
}

// ─────────────────────────────────────────────────────────────
// APPROACH 2 — Full Union-Find with rank
// More code, but scales to large n and is reusable across problems.
// ─────────────────────────────────────────────────────────────
class Solution {
    fun findRedundantConnection(edges: Array<IntArray>): IntArray {
        val n = edges.maxOf { it.max() } + 1
        val uf = UnionFind(n)

        for ((u, v) in edges) {
            // union() returns false when u and v are already connected
            // → this edge is redundant → return it immediately
            if (!uf.union(u, v)) return intArrayOf(u, v)
        }

        return intArrayOf(-1, -1) // unreachable given valid input
    }

    // ── Union-Find (Disjoint Set Union) ──────────────────────
    // Tracks which nodes belong to the same connected component.
    // Two optimisations keep every operation near O(1):
    //   1. Path compression  — find() flattens the tree as it walks up
    //   2. Union by rank     — always attach the shorter tree under the taller one
    class UnionFind(n: Int) {

        private val parent = IntArray(n) { it }  // parent[i] = i  →  i is its own root
        private val rank = IntArray(n) { 0 }   // rank ≈ tree height estimate

        // Returns the root of x's component (with path compression)
        fun find(x: Int): Int {
            if (parent[x] != x) parent[x] = find(parent[x])
            return parent[x]
        }

        // Merges the components of x and y.
        // Returns false if they were already in the same component (cycle detected).
        fun union(x: Int, y: Int): Boolean {
            val rx = find(x)
            val ry = find(y)
            if (rx == ry) return false  // already connected

            // Attach shorter tree under taller to keep structure flat
            when {
                rank[rx] < rank[ry] -> parent[rx] = ry
                rank[rx] > rank[ry] -> parent[ry] = rx
                else -> {
                    parent[ry] = rx; rank[rx]++
                }  // equal height → pick one, bump rank
            }
            return true
        }

        fun connected(x: Int, y: Int) = find(x) == find(y)
        fun componentCount() = parent.indices.count { find(it) == it }
    }
}
