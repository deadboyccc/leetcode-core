package three41

class Eager {

    class NestedIterator(nestedList: List<NestedInteger>) : Iterator<Int> {

        // Flatten everything eagerly into a simple list on construction.
        private val flattened = ArrayDeque<Int>()

        init {
            flatten(nestedList)
        }

        private fun flatten(list: List<NestedInteger>) {
            for (item in list) {
                if (item.isInteger()) flattened.addLast(item.getInteger()!!)
                else flatten(item.getList()!!)
            }
        }

        override fun hasNext() = flattened.isNotEmpty()
        override fun next() = flattened.removeFirst()
    }
}


/**
 * Interface for a data structure that can hold either a single integer
 * or a nested list of other NestedIntegers.
 */
interface NestedInteger {

    /** @return true if this NestedInteger holds a single integer, rather than a nested list. */
    fun isInteger(): Boolean

    /** * @return the single integer that this NestedInteger holds, if it holds a single integer.
     * Return null if this NestedInteger holds a nested list.
     */
    fun getInteger(): Int?

    /** Set this NestedInteger to hold a single integer. */
    fun setInteger(value: Int)

    /** Set this NestedInteger to hold a nested list and add a nested integer to it. */
    fun add(ni: NestedInteger)

    /** * @return the nested list that this NestedInteger holds, if it holds a nested list.
     * Return null if this NestedInteger holds a single integer.
     */
    fun getList(): List<NestedInteger>?
}


class NestedIterator(nestedList: List<NestedInteger>) : Iterator<Int> {

    // each stack frame is one list-level's iterator; we descend lazily
    private val stack = ArrayDeque<Iterator<NestedInteger>>()
        .also { it.addLast(nestedList.iterator()) }

    private var nextInt: Int? = null

    init {
        advance()
    }

    // drill down until we park a plain integer in nextInt, or exhaust everything
    private fun advance() {
        nextInt = null
        while (stack.isNotEmpty()) {
            val it = stack.last()
            if (!it.hasNext()) {
                stack.removeLast(); continue
            }  // level done, pop
            val curr = it.next()
            if (curr.isInteger()) {
                nextInt = curr.getInteger(); return
            }  // parked
            curr.getList()?.let { stack.addLast(it.iterator()) }  // descend
        }
    }

    override fun hasNext() = nextInt != null
    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return nextInt!!.also { nextInt = null; advance() }  // return, then pre-fetch
    }
}
