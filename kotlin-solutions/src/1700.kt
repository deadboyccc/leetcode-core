package one700;

class Solution {
    fun countStudents(students: IntArray, sandwiches: IntArray): Int {
        val studentDeque = ArrayDeque<Int>()
        val sandwichDeque = ArrayDeque<Int>()

        // addLast -> removeFirst (Queue)
        students.forEach { student -> studentDeque.addLast(student) }
        sandwiches.forEach { sandwich -> sandwichDeque.addLast(sandwich) }

        while (sandwichDeque.isNotEmpty()) {
            val currSandwich = sandwichDeque.first()
            var stuck = 0
            val snapshot = studentDeque.size  // capture before inner loop

            // 0 stuck out of 10
            // loop 10 times, check each, if all can't eat -> gets out of the while loop
            // num of students stuck = num of students in the snapshot ( q size ) = break the whole while loop
            // and return the student count who remained stuck
            // otherwise if one student = current sandwich  -> just pop that sandwich and break the inner while loop
            while (stuck < snapshot) {
                val currStudent = studentDeque.removeFirst()
                if (currStudent == currSandwich) {
                    sandwichDeque.removeFirst()
                    break
                } else {
                    studentDeque.addLast(currStudent)
                    stuck++
                }
            }

            if (stuck == snapshot) break
        }
        return studentDeque.size

    }
}