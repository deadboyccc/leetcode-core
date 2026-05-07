package misc.practie.generateSequence

import misc.kia_part_one.printSeperator
import java.util.Random
import kotlin.collections.ArrayDeque


fun main() {
    val seq1 = generateSequence(1) {
        if (it > 5) it else null
    }
    println("seq1: ${seq1.toList()}")

    val se2 = generateSequence {
        Random().nextInt(10).also { println("Generating: $it") }
    }
        .take(5) // Limit the sequence so it isn't infinite
        .toList() // Terminal operation: converts to List

    println("seq2: $se2")

    printSeperator()
    printSeperator()
    val seq3 = generateSequence {
        val ran =
            Random().nextInt(10).also { println("Generating: $it") }
        if (ran == 5) null else ran


    }.toList()
    println("seq3: ${seq3}")
    // so generateSequence keep going until it hits a null
    // or if the seq is infinite you have to .take(n)
    // it's lazy so , so on collecting .toList() -> terminal op seq is generated
    printSeperator()


}

class RecentCounter {

    private val window = ArrayDeque<Int>()

    fun ping(currentTime: Int): Int =
        window.run {

            // Add current request
            addLast(currentTime)

            /*
             * Generate values while the front item
             * is outside the valid time window.
             */

            generateSequence {
                firstOrNull()
                    ?.takeIf { it < currentTime - 3000 }

                // loop for the length of values that are invalid and removeFirst() so we end up with valid items
            }.forEach {

                // Remove outdated request
                removeFirst()
            }

            // Remaining requests are valid
            size
        }
}
