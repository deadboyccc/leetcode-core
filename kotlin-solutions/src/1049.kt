package one049

import misc.kia_part_one.printSeperator

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