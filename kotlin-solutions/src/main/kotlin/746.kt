package seven46

class OptimizedReadableSolution {
    fun minCostClimbingStairs(cost: IntArray): Int {
        // 'downOne' is the min cost to reach the step 1 level below current
        // 'downTwo' is the min cost to reach the step 2 levels below current
        var downTwo = 0
        var downOne = 0

        // We iterate through every step in the cost array.
        // At each step, we calculate the minimum cost to reach it and "step off" it.
        for (i in 2..cost.size) {
            // The cost to reach the current step 'i' is the min of the two previous
            // paths, adding the cost of the step we are jumping FROM.
            val current = minOf(downOne + cost[i - 1], downTwo + cost[i - 2])

            // Move our window forward:
            // The old 'downOne' becomes the new 'downTwo' for the next iteration
            downTwo = downOne
            downOne = current
        }

        return downOne
    }
}

class OptimizedSolution {
    fun minCostClimbingStairs(cost: IntArray): Int {

        // These represent the minimum cost to reach the previous two steps
        // You can start for free at idx 0, 1
        // [ prev2,  prev1,  currStep]
        var prev2 = 0
        var prev1 = 0

        // Iterate through every cost in the array
        for (i in 0 until cost.size) {
            // The cost to reach the NEXT step is current cost + min of previous two
            val current = cost[i] + minOf(prev1, prev2)

            // Shift our window forward
            prev2 = prev1
            prev1 = current
        }

        // The top of the floor can be reached from either of the last two steps
        return minOf(prev1, prev2)
    }
}

class class1D {
    class Solution {
        fun minCostClimbingStairs(cost: IntArray): Int {
            val n = cost.size
            // dp[i] will store the minimum cost to reach step i
            val dp = IntArray(n + 1)

            // You can start at index 0 or index 1 for free
            dp[0] = 0
            dp[1] = 0

            for (i in 2..n) {

                // To reach step i, you could have come from i-1 or i-2
                // You must pay the cost of the step you are leaving
                val option1 = dp[i - 1] + cost[i - 1]
                val option2 = dp[i - 2] + cost[i - 2]
                dp[i] = minOf(option1, option2)
            }

            return dp[n]
        }
    }
}

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
