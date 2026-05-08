package misc.LRUReview.practice

class LRUCache(private val capacity: Int) {
    class Node(val key: Int, var value: Int) {
        var prev: Node? = null
        var next: Node? = null
    }

    private val map = mutableMapOf<Int, Node>()
    private val head = Node(-1, -1) // LRU Side
    private val tail = Node(-1, -1) // MRU Side

    init {
        head.next = tail
        tail.prev = head
    }

    fun get(key: Int): Int {
        val node = map[key] ?: return -1
        promoteToTail(node)
        return node.value
    }

    fun put(key: Int, value: Int) {
        if (map.containsKey(key)) {
            val node = map[key]!!
            node.value = value
            promoteToTail(node)
        } else {
            if (map.size == capacity) {
                evictFromHead()
            }
            val newNode = Node(key, value)
            map[key] = newNode
            insertAtTail(newNode)
        }
    }

    // Moves an existing node to the MRU (tail) position
    private fun promoteToTail(node: Node) {
        unlink(node)
        insertAtTail(node)
    }

    // Removes the "oldest" node (the one right after head)
    private fun evictFromHead() {
        val lru = head.next ?: return
        if (lru == tail) return

        unlink(lru)
        map.remove(lru.key)
    }

    // Standard DLL unlink: connects the node's neighbors to each other
    private fun unlink(node: Node) {
        val prevNode = node.prev
        val nextNode = node.next
        prevNode?.next = nextNode
        nextNode?.prev = prevNode
    }

    // Inserts the node right before the tail sentinel
    private fun insertAtTail(node: Node) {
        val lastRealNode = tail.prev

        lastRealNode?.next = node
        node.prev = lastRealNode

        node.next = tail
        tail.prev = node
    }
}
