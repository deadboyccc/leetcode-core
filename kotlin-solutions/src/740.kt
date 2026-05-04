package seven40

class Solution {
    fun deleteAndEarn(nums: IntArray): Int {
        val pointsByValue = nums.groupBy { it }.mapValues { (value, list) -> value * list.size }
        val maxValue = nums.max()

        val memo = mutableMapOf<Int, Int>()

        fun dfs(value: Int): Int {
            if (value <= 0) return 0
            return memo.getOrPut(value) {
                val take = pointsByValue.getOrDefault(value, 0) + dfs(value - 2)
                val skip = dfs(value - 1)
                maxOf(take, skip)
            }
        }

        return dfs(maxValue)
    }
}


class ReadableCommentedSolution {
    fun deleteAndEarn(nums: IntArray): Int {
        // Taking value n earns n * frequency(n), but forbids n-1 and n+1
        // This is identical to House Robber on a value-indexed array
        val earn = nums.groupBy { it }.mapValues { (v, list) -> v * list.size }
        val memo = mutableMapOf<Int, Int>()

        fun maxEarnings(value: Int): Int {
            if (value <= 0) return 0
            return memo.getOrPut(value) {
                // At each value, we either take it (and skip value-1) or skip it
                val take = earn.getOrDefault(value, 0) + maxEarnings(value - 2)
                val skip = maxEarnings(value - 1)
                maxOf(take, skip)
            }
        }

        return maxEarnings(nums.max())
    }
}