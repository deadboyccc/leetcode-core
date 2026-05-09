package misc.unboundedKnapsack

class Solution {

    fun solveUnboundedKnapsack(capacity: Int, weights: IntArray, values: IntArray): Int {
        val n = weights.size
        if (n == 0 || capacity == 0) return 0

        // dp[i][j] = max value using items up to index 'i' with capacity 'j'
        val dp = Array(n) { IntArray(capacity + 1) }

        for (i in 0 until n) {
            val weight = weights[i]
            val value = values[i]

            for (j in 1..capacity) {
                // Option 1: Skip the current item (take value from previous item row)
                val skip = if (i > 0) dp[i - 1][j] else 0

                // Option 2: Take the current item (if it fits)
                // We look at dp[i][j - weight] because we can reuse this item
                val take = if (j >= weight) value + dp[i][j - weight] else 0

                dp[i][j] = maxOf(skip, take)
            }
        }

        return dp[n - 1][capacity]
    }
}

/*
 * **************************************************************************
 *                          UNBOUNDED KNAPSACK PROBLEM                      *
 * **************************************************************************
 *
 *  PROBLEM STATEMENT:
 *  You have a knapsack with a weight capacity W. There are n item types,
 *  each with a weight and a value. You can pick any item any number of
 *  times. Maximize the total value without exceeding W.
 *
 *  ------------------------------------------------------------------------
 *  TEST CASES:
 *
 *  1. Warm-up
 *     W = 10, items = [(w=2, v=3), (w=3, v=4), (w=4, v=5)]
 *     Expected Output: 15
 *
 *  2. One item dominates
 *     W = 15, items = [(w=5, v=10), (w=3, v=4), (w=7, v=8)]
 *     Expected Output: 30
 *
 *  3. Fractional-looking trap
 *     W = 11, items = [(w=2, v=5), (w=3, v=6), (w=5, v=11)]
 *     Expected Output: 27
 *
 *  4. Large W, many items
 *     W = 20, items = [(w=1, v=1), (w=3, v=4), (w=4, v=5), (w=5, v=7)]
 *     Expected Output: 28
 *
 *  5. Edge: Single item, fits repeatedly
 *     W = 12, items = [(w=4, v=9)]
 *     Expected Output: 27
 *
 *  6. Edge: No item fits
 *     W = 2, items = [(w=5, v=10), (w=3, v=4)]
 *     Expected Output: 0
 *
 *  ------------------------------------------------------------------------
 *  ALGORITHMIC FOCUS:
 *  - Define what dp[w] represents.
 *  - Determine the recurrence relation.
 *  - Observe how iteration order differs from 0/1 Knapsack.
 *  - Analyze Time and Space Complexity.
 * **************************************************************************
 */
