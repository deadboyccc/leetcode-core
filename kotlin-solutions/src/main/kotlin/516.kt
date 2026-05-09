package five16

class Solution {
    fun longestPalindromeSubseq(s: String): Int {
        val n = s.length
        // dp[i][j] will store the length of LPS in s[i..j]
        val dp = Array(n) { IntArray(n) }

        // Every single character is a palindrome of length 1
        for (i in 0 until n) {
            dp[i][i] = 1
        }

        // Build the table. 'len' is the length of the interval we are looking at.
        for (len in 2..n) {
            for (i in 0 until n - len + 1) {
                val j = i + len - 1

                if (s[i] == s[j]) {
                    // If the ends match, they add 2 to the LPS of the inner string
                    dp[i][j] = dp[i + 1][j - 1] + 2
                } else {
                    // If they don't match, we take the best result by skipping either i or j
                    dp[i][j] = maxOf(dp[i + 1][j], dp[i][j - 1])
                }
            }
        }

        return dp[0][n - 1]
    }
}

fun main() {
    val result = Solution().longestPalindromeSubseq("bbbab")
    println("Longed Palindromic Subsequence length: $result") // Output: 4 ("bbbb")
}
