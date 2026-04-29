package seven46

class TabulationSolution {
    class Solution {
        fun minCostClimbingStairs(cost: IntArray): Int {
            val n = cost.size

            // We use a fold to keep track of (minCostAtStep_i-2, minCostAtStep_i-1)
            // We start from index 2 because we can start for free at index 0 or 1.
            val finalCosts =
                (2..n).fold(cost[0] to cost[1]) { (prev2, prev1), i ->

                    // To get to step i, we take the minimum of the two previous paths
                    // If i == n, we don't add a 'cost[i]' because we are at the top.
                    val currentStepCost = if (i < n) cost[i] else 0
                    val currentMin = currentStepCost + minOf(prev1, prev2)

                    // Slide the window: prev1 becomes the new prev2, current becomes new prev1
                    prev1 to currentMin
                }

            // The result is the minimum cost to have reached the 'top' (index n)
            return finalCosts.second
        }
    }
}

class Solution {
    fun minCostClimbingStairs(cost: IntArray): Int {
        val n = cost.size
        // memo[i] stores the minimum cost to reach the top starting from index i
        val memo = IntArray(n) { -1 }

        fun dfs(index: Int): Int {
            // Base Case: If we jump past the last step, we've reached the top.
            // There is no additional cost beyond the array bounds.
            if (index >= n) return 0

            // Return cached result to avoid O(2^n) exponential explosion
            if (memo[index] != -1) return memo[index]

            // RECURRENCE RELATION:
            // f(i) = cost[i] + min(f(i+1), f(i+2))
            // We pay for the current step, then choose the cheapest next jump.
            val costOfNextSteps = minOf(dfs(index + 1), dfs(index + 2))

            memo[index] = cost[index] + costOfNextSteps
            return memo[index]
        }

        // The problem states we can start at index 0 OR index 1.
        // We return the minimum cost path originating from either.
        return minOf(dfs(0), dfs(1))
    }
}
