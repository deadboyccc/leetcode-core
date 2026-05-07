package four94

// DP- Top Down
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