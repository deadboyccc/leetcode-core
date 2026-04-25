fun minNumberOfSemesters(n: Int, relations: Array<IntArray>, k: Int): Int {
    // encode prerequisites as bitmasks: prereq[i] has bit j set if course j must come before course i
    val prereq = IntArray(n) { 0 }
    relations.forEach { (prev, next) ->
        prereq[next - 1] = prereq[next - 1] or (1 shl (prev - 1))
    }

    // dp[mask] = min semesters to complete exactly the courses in `mask`
    // mask is an n-bit number; bit i set means course i+1 is done
    val dp = IntArray(1 shl n) { Int.MAX_VALUE }
    dp[0] = 0  // base case: 0 courses done = 0 semesters

    for (mask in 0 until (1 shl n)) {
        if (dp[mask] == Int.MAX_VALUE) continue

        // find all courses available to take given the courses done in `mask`
        // a course is available if: not yet done AND all its prerequisites are in mask
        var available = 0
        for (course in 0 until n) {
            val notYetDone = (mask shr course) and 1 == 0
            val prereqsMet = (prereq[course] and mask) == prereq[course]
            if (notYetDone && prereqsMet) available = available or (1 shl course)
        }

        // enumerate all subsets of `available` with at most k bits set
        // each subset represents a valid choice of courses for one semester
        var subset = available
        while (subset > 0) {
            if (subset.countOneBits() <= k) {
                // taking this subset completes mask | subset after one more semester
                dp[mask or subset] = minOf(dp[mask or subset], dp[mask] + 1)
            }
            // standard bitmask trick to iterate all subsets of `available`
            subset = (subset - 1) and available
        }
    }

    // all n courses done = all bits set
    return dp[(1 shl n) - 1]
}