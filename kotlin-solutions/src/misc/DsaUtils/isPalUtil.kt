package misc.pallindrome

/**
 * INTERVIEW STANDARD: O(n²) Time | O(1) Space
 * Highly recommended for most coding interviews.
 */
class StandardPalindromeSolver {

    // Returns the total character count of the longest palindrome
    fun getLength(str: String): Int {
        if (str.isEmpty()) return 0
        var maxLen = 0
        for (i in str.indices) {
            // Check odd centers (i) and even centers (i, i+1)
            val len = maxOf(
                expand(str, i, i),
                expand(str, i, i + 1)
            )
            maxLen = maxOf(maxLen, len)
        }
        return maxLen
    }

    // Returns the actual text of the longest palindrome
    fun getString(str: String): String {
        if (str.length < 2) return str
        var start = 0
        var maxLen = 0
        for (i in str.indices) {
            val len = maxOf(
                expand(str, i, i),
                expand(str, i, i + 1)
            )
            if (len > maxLen) {
                maxLen = len
                // Shift start index backwards based on radius
                start = i - (len - 1) / 2
            }
        }
        return str.substring(start, start + maxLen)
    }

    private fun expand(s: String, left: Int, right: Int): Int {
        var l = left
        var r = right
        // Expand outwards as long as characters match
        while (l >= 0 && r < s.length && s[l] == s[r]) {
            l--
            r++
        }
        // Length formula: (r - 1) - (l + 1) + 1
        return r - l - 1
    }
}

/**
 * OPTIMAL PERFORMANCE: O(n) Time | O(n) Space
 * Uses Manacher's Algorithm to avoid redundant scans via mirroring.
 */
class ManacherPalindromeSolver {

    // Returns the length of the longest palindrome in linear time
    fun getLength(s: String): Int {
        if (s.isEmpty()) return 0
        val p = computeRadii(s)
        return p.maxOrNull() ?: 0 // Max radius in transformed string = max length in original
    }

    // Returns the actual substring in linear time
    fun getString(s: String): String {
        if (s.length < 2) return s
        val p = computeRadii(s)
        val maxLen = p.maxOrNull() ?: 0
        val centerIndex = p.indexOf(maxLen)
        // Map transformed center back to original string start
        val start = (centerIndex - maxLen) / 2
        return s.substring(start, start + maxLen)
    }

    private fun computeRadii(s: String): IntArray {
        // Transform "aba" -> "#a#b#a#" to handle even/odd symmetry
        val t = s.toList().joinToString(separator = "#", prefix = "#", postfix = "#")
        val n = t.length
        val p = IntArray(n) // Stores palindrome radius at each index
        var center = 0
        var boundary = 0

        for (i in 0 until n) {
            // If i is within current boundary, use mirrored value to save time
            if (i < boundary) {
                p[i] = minOf(boundary - i, p[2 * center - i])
            }
            // Attempt to expand further
            while (i - (p[i] + 1) >= 0 && i + (p[i] + 1) < n && t[i - (p[i] + 1)] == t[i + (p[i] + 1)]) {
                p[i]++
            }
            // Update center/boundary if this palindrome reaches further right
            if (i + p[i] > boundary) {
                center = i
                boundary = i + p[i]
            }
        }
        return p
    }
}

fun main() {
    val std = StandardPalindromeSolver()
    val opt = ManacherPalindromeSolver()
    val input = "racecar"

    println("Input: $input")
    println("Standard -> Length: ${std.getLength(input)}, String: ${std.getString(input)}")
    println("Manacher -> Length: ${opt.getLength(input)}, String: ${opt.getString(input)}")
}
