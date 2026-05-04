package four5

class Solution {
    fun jump(nums: IntArray): Int {
        val lastIndex = nums.lastIndex
        val memo = IntArray(nums.size) { -1 }

        /**
         * Returns the minimum jumps needed to reach the end starting from [current]
         */
        fun minJumpsFrom(current: Int): Int {
            // Base case: We are already at or past the finish line
            if (current >= lastIndex) return 0

            // If we've calculated this index before, return it immediately
            if (memo[current] != -1) return memo[current]

            var bestResult = 10001 // A value larger than any possible path (max N is 10,000)

            // Look at every possible jump distance available at this index
            val maxJump = nums[current]
            val furthestReach = minOf(current + maxJump, lastIndex)

            for (nextStep in (current + 1)..furthestReach) {
                val jumpsFromNextStep = minJumpsFrom(nextStep)

                // If a valid path exists from the next step, update our best result
                if (jumpsFromNextStep != 10001) {
                    bestResult = minOf(bestResult, 1 + jumpsFromNextStep)
                }
            }

            memo[current] = bestResult
            return bestResult
        }

        return minJumpsFrom(0)
    }
}
