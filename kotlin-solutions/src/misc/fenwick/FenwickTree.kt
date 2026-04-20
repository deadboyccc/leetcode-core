package misc.fenwick

class FenwickTree(private val arr: IntArray) {
    // We use arr.size + 1 because Fenwick Trees are 1-indexed for bit manipulation
    private val tree = LongArray(arr.size + 1)

    init {
        // Step 1: Copy values into tree (shifted by 1)
        for (i in arr.indices) {
            tree[i + 1] = arr[i].toLong()
        }

        // Step 2: The O(n) trick - Push sums to the immediate parent
        for (i in 1..arr.size) {
            val parent = i + (i and -i)
            if (parent <= arr.size) {
                tree[parent] += tree[i]
            }
        }
    }

    /**
     * Standard update: Adds 'delta' to the element at 'index'
     * and propagates the change to all affected ancestors.
     */
    fun update(index: Int, delta: Long) {
        var i = index
        while (i <= arr.size) {
            tree[i] += delta
            i += i and -i
        }
    }


    fun query(index: Int): Long {
        var i = index
        var sum = 0L
        while (i > 0) {
            sum += tree[i]
            i -= i and -i
        }
        return sum
    }

    fun rangeQuery(low: Int, high: Int): Long = query(high) - query(low - 1)

    // 0-based convenience wrappers

    /**
     * Point update for the original array:
     * Calculates delta automatically and updates the internal arr.
     */
    fun pointUpdate0(index: Int, newValue: Int) {
        val delta = newValue.toLong() - arr[index]
        arr[index] = newValue
        update(index + 1, delta)
    }

    fun query0(i: Int): Long = query(i + 1)
    fun rangeQuery0(l: Int, r: Int): Long = rangeQuery(l + 1, r + 1)
}
