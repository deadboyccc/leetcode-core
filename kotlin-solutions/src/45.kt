package four5

class BottomUpDpSolution {

    fun jump(nums: IntArray): Int {
        val n = nums.size

        // dp[i] = minimum number of jumps needed to reach index i
        val dp = IntArray(n) { Int.MAX_VALUE }
        dp[0] = 0 // starting position requires 0 jumps

        for (i in 0 until n) {

            // Determine reachable range from index i
            val firstReachableIndex = i + 1
            val lastReachableIndex = minOf(i + nums[i], n - 1)

            // Try all reachable positions from i
            for (targetIndex in firstReachableIndex..lastReachableIndex) {

                // One more jump from i to targetIndex
                val jumpsViaCurrent = dp[i] + 1

                // Update with the minimum jumps needed
                dp[targetIndex] = minOf(dp[targetIndex], jumpsViaCurrent)
            }
        }

        return dp[n - 1]
    }
}

class GreedySolution {
    fun jump(nums: IntArray): Int {
        // [ mark blocks of regions reachable by a 1], max # of blocks = max jumps (reachability is guaranteed}
        // our first block is [0,0] reachable by zero jumps
        var res = 0 // num of jumps so far
        var left = 0
        var right = 0
        while (right < nums.size - 1) {
            var farthest = 0
            for (i in left..right) {
                // farthest =max of furthest or the current index + its max jump
                farthest = maxOf(farthest, i + nums[i])
            }
            // now that we have the correct farthest from the current block
            // update left to be right+1
            // update right to be farthest
            left = right + 1

            // loop condition breaker if we pass the last index -> we are guaranteed to do so
            // we update for every block change ( for loop) -> res+=1
            right = farthest
            res += 1


        }
        return res


    }
}

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
