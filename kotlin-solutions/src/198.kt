package one98

class MostOptimized {
    fun rob(nums: IntArray): Int {
        // [ prev2, prev1, currRobt]
        var prev2 = 0 // Best if we stopped 2 houses ago
        var prev1 = 0 // Best if we stopped 1 house ago

        for (num in nums) {
            // Recurrence: Max of (robbing current + 2 houses ago) OR (skipping current)
            val current = maxOf(num + prev2, prev1)

            // Shift states forward
            prev2 = prev1
            prev1 = current
        }

        return prev1
    }
}

class ReadableSolution {
    fun rob(nums: IntArray): Int =
        nums.fold(0 to 0) { (twoBack, oneBack), houseValue ->

            // slide the window: twoBack is discarded, oneBack becomes the new twoBack
            val newTwoBack = oneBack
            // either skip this house (oneBack) or rob it (twoBack + houseValue)
            val newOneBack = maxOf(oneBack, twoBack + houseValue)

            newTwoBack to newOneBack
        }.second
}

class SolutionMemo {
    fun rob(nums: IntArray): Int {
        val memo = mutableMapOf<Int, Int>()
        fun dfs(i: Int): Int {
            if (i !in nums.indices) return 0
            return memo.getOrPut(i) {
                maxOf(dfs(i + 1), nums[i] + dfs(i + 2))
            }
        }
        return dfs(0)
    }
}

// non mem
class dfsSolution {
    // dfs = sum , out of index = 0
    fun rob(nums: IntArray): Int {
        fun dfs(i: Int): Int {
            if (i !in nums.indices) return 0
            return maxOf(dfs(i + 1), nums[i] + dfs(i + 2))
        }
        return dfs(0)
    }
}

class Solution {
    fun rob(nums: IntArray): Int =
        nums.fold(0 to 0) { (prev2, prev1), n ->
            prev1 to maxOf(prev1, prev2 + n)
        }.second
}