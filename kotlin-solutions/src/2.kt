package two

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

class SolutionReadable {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var p1 = l1
        var p2 = l2
        val dummy = ListNode(0)
        var curr = dummy
        var carry = 0

        // 1. Process both lists while they both have nodes
        while (p1 != null && p2 != null) {
            val sum = p1.`val` + p2.`val` + carry
            carry = sum / 10
            curr.next = ListNode(sum % 10)

            curr = curr.next!!
            p1 = p1.next
            p2 = p2.next
        }

        // 2. Handle the remainder of L1 (if it was longer)
        var remaining = if (p1 != null) p1 else p2
        while (remaining != null) {
            val sum = remaining.`val` + carry
            carry = sum / 10
            curr.next = ListNode(sum % 10)

            curr = curr.next!!
            remaining = remaining.next
        }

        // 3. Final check: if there's still a carry left over
        if (carry > 0) {
            curr.next = ListNode(carry)
        }

        return dummy.next
    }
}

class Solution {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        val dummy = ListNode(0)
        var curr = dummy
        var p1 = l1
        var p2 = l2
        var carry = 0

        // As long as there is a digit to process or a carry to add
        while (p1 != null || p2 != null || carry != 0) {
            val sum = (p1?.`val` ?: 0) + (p2?.`val` ?: 0) + carry

            carry = sum / 10
            curr.next = ListNode(sum % 10)

            // Move all pointers forward
            curr = curr.next!!
            p1 = p1?.next
            p2 = p2?.next
        }

        return dummy.next
    }
}
