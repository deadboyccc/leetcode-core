package two06

/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 */
class ListNode(var `val`: Int) {
    var next: ListNode? = null
}


class SolutionIterative {
    fun reverseList(head: ListNode?): ListNode? {
        var prev: ListNode? = null
        var curr = head

        while (curr != null) {
            // 1. Bookmark the next node (so we don't lose the list)
            val nextTemp = curr.next

            // 2. Flip the pointer (the actual reversal)
            curr.next = prev

            // 3. Move the window forward
            prev = curr
            curr = nextTemp
        }

        // prev will be the new head because curr finished at null
        return prev
    }
}

// recursive
class Solution {
    fun reverseList(head: ListNode?): ListNode? {
        // Base case: if list is empty or we are at the last node
        if (head?.next == null) return head

        // Recursive step: find the new head (the original tail)
        val newHead = reverseList(head.next)

        // The "Flip": Make the next node point back to the current node
        // head.next is the node after me; (head.next).next is that node's pointer
        head.next?.next = head

        // The "Cut": Set the current node's next to null to prevent cycles
        head.next = null

        return newHead
    }
}
