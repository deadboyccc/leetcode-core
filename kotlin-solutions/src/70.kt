package seven0;

class Solution {
    // We use a Map or an Array to store results.
    // An Array is faster since our keys are just numbers 0 to n.
    private lateinit var memo: IntArray

    fun climbStairs(n: Int): Int {
        memo = IntArray(n + 1) { -1 } // Initialize with -1 to indicate "uncalculated"
        return helper(n)
    }

    private fun helper(n: Int): Int {
        // Base cases
        if (n <= 1) return 1

        // If we've already calculated this step, return the stored value
        if (memo[n] != -1) return memo[n]

        // Otherwise, calculate, store, and return
        memo[n] = helper(n - 1) + helper(n - 2)
        return memo[n]
    }
}
