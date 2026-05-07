package one41

/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 */
class ListNode(var `val`: Int) {
    var next: ListNode? = null
}


// visited set
class Solution {
    val visited = mutableSetOf<ListNode?>()
    fun hasCycle(head: ListNode?): Boolean {
        var head = head
        while (head?.next?.next != null) {
            if (head.next == null) return false
            if (visited.contains(head.next)) {
                return true
            }
            visited.add(head.next)
            head = head.next

        }
        return false
    }
}

// Tortoise and Hare (Floyd's Cycle Detection)
// Two pointers: slow & fast
// O(1) extra memory

class Solution2 {


    fun hasCycle(head: ListNode?): Boolean {
        var slow = head
        var fast = head

        while (fast?.next != null) {
            slow = slow?.next
            fast = fast.next?.next

            if (slow == fast) {
                return true
            }
        }

        return false
    }
}
// SIMPLE DRY RUN
// ------------------------
// (1)->(2)->(3)->(4)->(1)
// 1. slow = 1, fast = 1
// 2. slow = 2, fast = 3
// 3. slow = 3, fast = 1
// 4. slow = 4, fast = 3
// 5. slow = 1, fast = 1 == both equal == cycle
// ---------------------------------------------
fun main() {
    var nullable: TestNullability? = TestNullability()
//    nullable = null
    val theInt = nullable?.int ?: 0
    println(theInt)


}

class TestNullability(val int: Int = 10) {
}

