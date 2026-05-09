package one46

class LRUCache(private val capacity: Int) {

    private class Node(val key: Int, var value: Int) {
        var prev: Node? = null
        var next: Node? = null
    }

    private val map = HashMap<Int, Node>(capacity)
    private val head = Node(-1, -1)  // sentinel LRU end
    private val tail = Node(-1, -1)  // sentinel MRU end

    init {
        head.next = tail
        tail.prev = head
    }

    fun get(key: Int): Int {
        val node = map[key] ?: return -1
        moveToTail(node)
        return node.value
    }

    fun put(key: Int, value: Int) {
        map[key]?.let {
            it.value = value
            moveToTail(it)
            return
        }

        if (map.size == capacity) {
            val lru = head.next!!
            unlink(lru)
            map.remove(lru.key)
        }

        val node = Node(key, value).also { insertBeforeTail(it) }
        map[key] = node
    }

    private fun unlink(node: Node) {
        node.prev!!.next = node.next
        node.next!!.prev = node.prev
    }

    private fun insertBeforeTail(node: Node) {
        val prev = tail.prev!!
        prev.next = node
        node.prev = prev
        node.next = tail
        tail.prev = node
    }

    private fun moveToTail(node: Node) {
        unlink(node)
        insertBeforeTail(node)
    }
}
