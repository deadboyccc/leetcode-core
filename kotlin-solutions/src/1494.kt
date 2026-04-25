class Solution {
    fun minNumberOfSemesters(n: Int, relations: Array<IntArray>, k: Int): Int {
        val prereq = IntArray(n) { 0 }
        relations.forEach { (prev, next) ->
            prereq[next - 1] = prereq[next - 1] or (1 shl (prev - 1))
        }

        val dp = IntArray(1 shl n) { Int.MAX_VALUE }
        dp[0] = 0

        for (mask in 0 until (1 shl n)) {
            if (dp[mask] == Int.MAX_VALUE) continue

            var available = 0
            for (course in 0 until n) {
                val notDone = (mask shr course) and 1 == 0
                val prereqsMet = (prereq[course] and mask) == prereq[course]
                if (notDone && prereqsMet) available = available or (1 shl course)
            }

            var subset = available
            while (subset > 0) {
                if (subset.countOneBits() <= k) {
                    dp[mask or subset] = minOf(dp[mask or subset], dp[mask] + 1)
                }
                subset = (subset - 1) and available
            }
        }

        return dp[(1 shl n) - 1]
    }
}
