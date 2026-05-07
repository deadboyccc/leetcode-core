package fivelc

/**
 * INTERVIEW STANDARD: O(n²) Time | O(1) Space
 * Highly recommended for most coding interviews.
 */
class StandardPalindromeSolver {
    class Solution {
        fun longestPalindrome(str: String): Int {
            if (str.isEmpty()) return 0
            return getLength(str)
        }


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
}

class Solution {
    fun longestPalindrome(s: String): String {
        if (s.isEmpty()) return ""

        var start = 0
        var end = 0

        // Helper to expand outwards and return the length of the found palindrome
        fun expand(left: Int, right: Int): Int {
            var l = left
            var r = right
            while (l >= 0 && r < s.length && s[l] == s[r]) {
                l--
                r++
            }
            // Return length: (r - 1) - (l + 1) + 1
            return r - l - 1
        }

        for (i in s.indices) {
            // Check both odd (aba) and even (abba) centers
            val len = maxOf(expand(i, i), expand(i, i + 1))

            // If found a longer one, update the boundaries
            if (len > end - start) {
                start = i - (len - 1) / 2
                end = i + len / 2
            }
        }

        return s.substring(start, end + 1)
    }
}
