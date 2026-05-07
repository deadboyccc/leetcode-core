package four94

// DP- Top Down Memoization
class Solution {
    fun findTargetSumWays(nums: IntArray, target: Int): Int {
        // (index,sum) -> # of ways
        val mem = mutableMapOf<Pair<Int, Int>, Int>()


        // dfs=# of ways to get the target sum starting from the index to n
        fun dfs(index: Int = 0, sum: Int = 0): Int {

            // cache hit
            mem[index to sum]?.let { return it }

            if (index > nums.lastIndex) {
                return if (sum == target) 1 else 0
            }
            // return all the ways of taking/skipping(+,-) the next index
            val result = dfs(index + 1, sum + nums[index]) + dfs(index + 1, sum - nums[index])
            mem[index to sum] = result
            return result

        }
        return dfs()

    }
}

// DP - Bottom Up Tabulation (Space Optimized)
class TabSolution {
    fun findTargetSumWays(nums: IntArray, target: Int): Int {
        // Map stores: Current Sum -> Number of ways to reach it
        var dp = mutableMapOf<Int, Int>()

        // Base Case: 0 numbers used result in a  [[ sum of 0 in exactly 1 way ]]
        dp[0] = 1

        for (num in nums) {
            val nextDp = mutableMapOf<Int, Int>()
            // Iterate through every reachable sum from the previous step
            for ((currentSum, count) in dp) {
                // Option 1: Add the current number
                val plus = currentSum + num
                nextDp[plus] = nextDp.getOrDefault(plus, 0) + count

                // Option 2: Subtract the current number
                val minus = currentSum - num
                nextDp[minus] = nextDp.getOrDefault(minus, 0) + count
            }
            // Move to the next state (replaces your clear/putAll for efficiency)
            dp = nextDp
        }

        // Return ways to reach target; if target was never reached, return 0
        return dp[target] ?: 0
    }
}
