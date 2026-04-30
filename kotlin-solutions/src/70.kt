package seven0;

// optimized 2 vars dp
class OptimizedSolution {
    fun climbStairs(n: Int): Int {
        // [ prev2, prev1, currStep ]
        var prev2 = 1 // 1->
        var prev1 = 2 //1->1->  or 2->
        if (n <= 2) return n

        repeat(n - 2) {
            val currStep = prev1 + prev2

            // shift states
            prev2 = prev1
            prev1 = currStep
        }
        return prev1


    }


}
// generic 1D full array solution
class Solution1D {
    fun climbStairs(n: Int): Int {
        if (n == 1 || n == 2) return n
        val waysToClimb = IntArray(n + 1);
        waysToClimb[0] = 0
        waysToClimb[1] = 1
        waysToClimb[2] = 2
        for (i in 3..waysToClimb.lastIndex) {
            waysToClimb[i] += waysToClimb[i - 1] + waysToClimb[i - 2]
        }
        return waysToClimb[waysToClimb.lastIndex]

    }
}


fun climbStairs(n: Int): Int {
    if (n <= 2) return n
    return (3..n).fold(1 to 2) { (prev1, prev2), _ ->
        prev2 to (prev1 + prev2)
    }.second
}

class DPSolutionReadable {
    fun climbStairs(n: Int): Int {
        // Edge Case Handling:
        // If n = 1, there is only 1 way. If n = 2, there are 2 ways.
        // Your loop starts at 3, so we handle these cases manually.
        if (n <= 2) return n

        // We use .fold to keep track of the two previous results (prev1, prev2).
        // Initial state: (1 way to reach step 1, 2 ways to reach step 2)
        return (3..n).fold(1 to 2) { (prev1, prev2), _ ->

            // Current ways = ways to reach (n-1) + ways to reach (n-2)
            val current = prev1 + prev2

            // Shift the window forward:
            // The old prev2 becomes the new prev1
            // The current sum becomes the new prev2
            prev2 to current

        }.second // Return the second value of the pair (the result for step n)
    }
}

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
