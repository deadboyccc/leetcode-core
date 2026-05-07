package one352

// =========================
// 1. BRUTE FORCE (O(k))
// =========================
// Idea: multiply last k elements directly
// Time: O(k) per query, Space: O(n)
class ProductOfNumbers {

    private val list = mutableListOf<Int>()

    fun add(num: Int) {
        list.add(num)
    }

    fun getProduct(k: Int): Int {
        var res = 1
        var i = list.lastIndex

        repeat(k) {
            res *= list[i--]
        }
        return res
    }
}


// =========================
// 2. PREFIX PRODUCT (OPTIMAL)
// =========================
// Idea: store cumulative product, reset on zero
// Query becomes division of two prefix values
// Time: O(1) add, O(1) getProduct
// Space: O(n)
class ProductOfNumbersPrefix {

    private val prefix = mutableListOf(1L)

    fun add(num: Int) {
        if (num == 0) {
            // reset because zero invalidates all previous products
            prefix.clear()
            prefix.add(1L)
        } else {
            prefix.add(prefix.last() * num)
        }
    }

    fun getProduct(k: Int): Int {
        // if k spans past reset point → zero exists in window
        if (k >= prefix.size) return 0

        val n = prefix.size
        return (prefix[n - 1] / prefix[n - 1 - k]).toInt()
    }
}


// =========================
// 3. CLEAN OPTIMIZED VARIANT (same idea)
// =========================
// Slightly more idiomatic version of prefix solution
class ProductOfNumbersClean {

    // prefix[i] = product up to i since last zero
    private val prefix = mutableListOf(1L)

    fun add(num: Int) {
        if (num == 0) {
            // restart segment after zero
            prefix.clear()
            prefix.add(1L)
            return
        }

        prefix.add(prefix.last() * num)
    }

    fun getProduct(k: Int): Int {
        if (k >= prefix.size) return 0

        val end = prefix.lastIndex
        return (prefix[end] / prefix[end - k]).toInt()
    }
}
