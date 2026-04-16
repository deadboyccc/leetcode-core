package two1;

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

class Solution {
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        // current is dummy
        // dummy
        // current
        //       actual start
        // so we return dummy .next

        // () -> ()->
        // () ->
        // 1. Create a dummy head to simplify the 'next' logic
        val dummy = ListNode(0)
        var current: ListNode? = dummy

        var l1 = list1
        var l2 = list2

        // 2. Traverse both lists until one runs out
        while (l1 != null && l2 != null) {
            if (l1.`val` <= l2.`val`) {
                current?.next = l1
                l1 = l1.next
            } else {
                current?.next = l2
                l2 = l2.next
            }
            current = current?.next
        }

        // 3. If one list is longer than the other, attach the remainder
        current?.next = l1 ?: l2

        // 4. Return the actual start of the merged list
        return dummy.next
    }
}
