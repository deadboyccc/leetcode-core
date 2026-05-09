package misc.DsaUtils

/**
 * Generic Segment Tree over a Monoid (combine + identity).
 *
 * Public API is fully 0-indexed: indices in [0, size).
 * Internal tree array is 1-indexed (root at node 1) for clean 2i / 2i+1 arithmetic.
 *
 * @param size     Number of logical elements.
 * @param identity Monoid identity element (returned on empty/out-of-range queries).
 * @param combine  Associative binary operation.
 */

class SegmentTree<T>(
    private val size: Int,
    private val identity: T,
    private val combine: (T, T) -> T,
) {
    // 4*size is safe upper bound for a 1-indexed implicit tree.
    @Suppress("UNCHECKED_CAST")
    private val tree: Array<T> = arrayOfNulls<Any>(4 * size) as Array<T>

    init {
        tree.fill(identity)
    }

    // ── Public API (0-indexed) ──────────────────────────────────────────────

    /** Build the tree from an existing array in O(n). */
    fun build(arr: Array<T>) {
        require(arr.size == size) { "Array size ${arr.size} must equal tree size $size" }
        buildInternal(arr, node = 1, l = 0, r = size - 1)
    }

    /** Point-update: set position [index] to [newValue]. O(log n). */
    fun update(index: Int, newValue: T) {
        requireIndex(index)
        updateInternal(index, newValue, node = 1, l = 0, r = size - 1)
    }

    /** Range-query: combine all elements in [ql, qr] inclusive. O(log n). */
    fun query(ql: Int, qr: Int): T {
        require(ql <= qr) { "ql=$ql must be ≤ qr=$qr" }
        requireIndex(ql); requireIndex(qr)
        return queryInternal(ql, qr, node = 1, l = 0, r = size - 1)
    }

    // ── Internal helpers (1-indexed node, 0-indexed element range) ──────────

    private fun buildInternal(arr: Array<T>, node: Int, l: Int, r: Int) {
        if (l == r) {
            tree[node] = arr[l]
            return
        }
        val mid = (l + r) / 2
        buildInternal(arr, 2 * node, l, mid)
        buildInternal(arr, 2 * node + 1, mid + 1, r)
        tree[node] = combine(tree[2 * node], tree[2 * node + 1])
    }

    private fun updateInternal(index: Int, newValue: T, node: Int, l: Int, r: Int) {
        if (l == r) {
            tree[node] = newValue
            return
        }
        val mid = (l + r) / 2
        if (index <= mid) updateInternal(index, newValue, 2 * node, l, mid)
        else updateInternal(index, newValue, 2 * node + 1, mid + 1, r)
        tree[node] = combine(tree[2 * node], tree[2 * node + 1])
    }

    private fun queryInternal(ql: Int, qr: Int, node: Int, l: Int, r: Int): T {
        if (qr < l || r < ql) return identity            // no overlap
        if (ql <= l && r <= qr) return tree[node]        // full overlap
        val mid = (l + r) / 2                            // partial overlap
        return combine(
            queryInternal(ql, qr, 2 * node, l, mid),
            queryInternal(ql, qr, 2 * node + 1, mid + 1, r),
        )
    }

    private fun requireIndex(i: Int) =
        require(i in 0 until size) { "Index $i out of bounds [0, $size)" }
}

// ── 0-index extension utils ─────────────────────────────────────────────────

/** Build a sum segment tree from an IntArray. */
fun segTreeSumInt(arr: IntArray): SegmentTree<Int> =
    SegmentTree(arr.size, identity = 0, combine = Int::plus)
        .also { it.build(arr.toTypedArray()) }

/** Build a sum segment tree from a LongArray. */
fun segTreeSumLong(arr: LongArray): SegmentTree<Long> =
    SegmentTree(arr.size, identity = 0L, combine = Long::plus)
        .also { it.build(arr.toTypedArray()) }

/** Build a min segment tree from an IntArray. */
fun segTreeMinInt(arr: IntArray): SegmentTree<Int> =
    SegmentTree(arr.size, identity = Int.MAX_VALUE, combine = ::minOf)
        .also { it.build(arr.toTypedArray()) }

/** Build a max segment tree from an IntArray. */
fun segTreeMaxInt(arr: IntArray): SegmentTree<Int> =
    SegmentTree(arr.size, identity = Int.MIN_VALUE, combine = ::maxOf)
        .also { it.build(arr.toTypedArray()) }

/** Build an empty sum segment tree of [size] longs, all initialised to 0. */
fun segTreeSumLong(size: Int): SegmentTree<Long> =
    SegmentTree(size, identity = 0L, combine = Long::plus)
