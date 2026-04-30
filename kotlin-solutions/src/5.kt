package fivelc

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
