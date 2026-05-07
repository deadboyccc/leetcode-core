package one049

import misc.kia_part_one.printSeperator
import kotlin.math.abs

// pure recursion no mem
class RSolution {
    fun lastStoneWeightII(stones: IntArray): Int {
        val stoneSum = stones.sum()
        val target = (stoneSum + 1) / 2

        fun dfs(i: Int, total: Int): Int {
            if (total >= target || i == stones.size) {
                return abs(total - (stoneSum - total))
            }
            return minOf(dfs(i + 1, total), dfs(i + 1, total + stones[i]))
        }

        return dfs(0, 0)
    }
}

class TopDown {
    class Solution {
        fun lastStoneWeightII(stones: IntArray): Int {
            val total = stones.sum()

            // We want to split stones into two groups S1 and S2 minimizing |S1 - S2|.
            // If S1 >= S2, the answer is S1 - S2 = total - 2*S2.
            // So we maximize S2 up to floor(total/2) — equivalent to a 0/1 knapsack.
            // Using (total+1)/2 as the ceiling target lets us DFS toward the midpoint
            // and compute the difference at any state where total >= target.
            val target = (total + 1) / 2

            // Memoization table: dp[i][carried] = minimum stone difference
            // achievable from index i onward, given `carried` weight in group S2.
            val dp = Array(stones.size) { IntArray(target + 1) { -1 } }

            fun dfs(i: Int, carried: Int): Int {
                // Base: either we've filled group S2 past the midpoint, or no stones remain.
                // The remaining stones implicitly form S1 = total - carried.
                if (carried >= target || i == stones.size) {
                    return abs(carried - (total - carried))
                }

                if (dp[i][carried] != -1) return dp[i][carried]

                // Either skip stones[i] (leave it in S1) or include it in S2.
                dp[i][carried] = minOf(
                    dfs(i + 1, carried),              // skip
                    dfs(i + 1, carried + stones[i])   // include
                )

                return dp[i][carried]
            }

            return dfs(0, 0)
        }
    }
}

class Solution {

    fun lastStoneWeightII(stones: IntArray): Int {

        val total = stones.sum()
        val half = total / 2

        // can we form exactly sum j using some subset of stones?
        val dp = BooleanArray(half + 1)
        dp[0] = true


        for (stone in stones) {
            for (j in half downTo stone) {       // backwards = each stone used once
                dp[j] = dp[j] || dp[j - stone]  // skip stone | take stone
            }
        }

        // largest subset sum <= half → optimal split minimizes leftover
        val best = (half downTo 0).first { dp[it] }
        return total - 2 * best  // group2(total-best) - group1(best)
    }
}


fun main() {
    val bolArr = booleanArrayOf(false, true, false, false, false, true)
    // find the index of the first true
    val firstTrueIdx = bolArr.indices.first { bolArr[it] }.also { println(it) }

    // find the index of the last first true
    val lastTrueIdx = bolArr.indices.last { bolArr[it] }.also { println(it) }


    printSeperator()
    val intArr = intArrayOf(1, 2, 3, 4, 5, 1)
    val firstAboveThree = intArr.first { it > 3 }
    val lastAboveThree = intArr.last { it > 3 }
    println(firstAboveThree)
    println(lastAboveThree)


    printSeperator()
    val arr = intArrayOf(1, 2, 3, 4, 5, 1)
    val doubleArr = DoubleArray(arr.size) { i -> arr[i].toDouble() }
    val maxInt = arr.maxBy { it / 2 }.also { println(it) }
    val maxDouble = doubleArr.maxBy { it / 2 }.also { println(it) }


}