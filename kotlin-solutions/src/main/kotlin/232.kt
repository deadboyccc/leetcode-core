package two32


class MyQueue() {

    private val pushStack = ArrayDeque<Int>()
    private val popStack = ArrayDeque<Int>()

    fun push(x: Int) {
        pushStack.addLast(x)
    }

    fun pop(): Int {
        if (popStack.isNotEmpty()) return popStack.removeLast()
        while (pushStack.isNotEmpty()) {
            popStack.addLast(pushStack.removeLast())
        }
        return popStack.removeLast()

    }

    fun peek(): Int {
        if (popStack.isNotEmpty()) return popStack.last()
        while (pushStack.isNotEmpty()) {
            popStack.addLast(pushStack.removeLast())
        }
        return popStack.last()

    }

    fun empty(): Boolean {
        return pushStack.isEmpty() && popStack.isEmpty()

    }

}

/**
 * Your MyQueue object will be instantiated and called as such:
 * var obj = MyQueue()
 * obj.push(x)
 * var param_2 = obj.pop()
 * var param_3 = obj.peek()
 * var param_4 = obj.empty()
 */