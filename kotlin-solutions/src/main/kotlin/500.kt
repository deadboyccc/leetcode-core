package five00

class Solution {
    fun findWords(words: Array<String>): Array<String> {
        val rows = listOf(
            "qwertyuiop".toSet(),
            "asdfghjkl".toSet(),
            "zxcvbnm".toSet()
        )

        return words.filter { word ->
            val lowerWord = word.lowercase()

            // Check if ANY row contains ALL characters of the word
            rows.any { row ->
                lowerWord.all { char -> char in row }
            }

        }.toTypedArray()
    }
}

fun main() {
    val input = arrayOf("Hello", "Alaska", "Dad", "Peace")
    val result = Solution().findWords(input)

    // Output: ["Alaska", "Dad"]
    println(result.joinToString(", "))
}
