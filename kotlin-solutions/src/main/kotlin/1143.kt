package one143

class SolutionCacheCommented {
    fun longestCommonSubsequence(text1: String, text2: String): Int {
        // Initialize DP table with -1 for memoization
        val dp = Array(text1.length) { IntArray(text2.length) { -1 } }

        fun dfs(idx1: Int, idx2: Int): Int {
            // Base case: if we run out of characters in either string
            if (idx1 >= text1.length || idx2 >= text2.length) return 0

            // Return cached result if available
            if (dp[idx1][idx2] != -1) return dp[idx1][idx2]

            if (text1[idx1] == text2[idx2]) {
                // If characters match, move both pointers forward
                dp[idx1][idx2] = 1 + dfs(idx1 + 1, idx2 + 1)
            } else {
                // If they don't match, try skipping one char from text1 OR text2
                dp[idx1][idx2] = maxOf(dfs(idx1 + 1, idx2), dfs(idx1, idx2 + 1))
            }

            return dp[idx1][idx2]
        }

        return dfs(0, 0)
    }
}

class SolutionCache {
    // top down brute force
    fun longestCommonSubsequence(text1: String, text2: String): Int {
        val dp = Array(text1.length) { IntArray(text2.length) { -1 } }
        // rows = text1.size , cols = text2.size
        // dp[i][j] = lcs for the given idx's

        fun dfs(idx1: Int, idx2: Int): Int {
            if (idx1 !in text1.indices || idx2 !in text2.indices) return 0
            if (dp[idx1][idx2] != -1) return dp[idx1][idx2]
            if (text1[idx1] == text2[idx2]) {
                dp[idx1][idx2] = 1 + dfs(idx1 + 1, idx2 + 1)
                return dp[idx1][idx2]
            }
            dp[idx1][idx2] = maxOf(dfs(idx1 + 1, idx2), dfs(idx1, idx2 + 1))
            return dp[idx1][idx2]
        }
        return dfs(0, 0)

    }
}

class Solution {
    // top down brute force
    fun longestCommonSubsequence(text1: String, text2: String): Int {

        fun dfs(idx1: Int, idx2: Int): Int {
            if (idx1 !in text1.indices || idx2 !in text2.indices) return 0
            if (text1[idx1] == text2[idx2]) return 1 + dfs(idx1 + 1, idx2 + 1)
            return maxOf(dfs(idx1 + 1, idx2), dfs(idx1, idx2 + 1))
        }
        return dfs(0, 0)

    }
}
