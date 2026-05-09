package five5
// ============================================================
// LeetCode 55 – Jump Game
// Can you reach the last index from index 0?
// nums[i] = max jump length from index i
// ============================================================


// ── 1. Brute-force recursion (no memo) ──────────────────────
// Try every possible jump from each index.
// T: O(2^n)  S: O(n) stack
fun canJumpBrute(nums: IntArray): Boolean {
    val n = nums.size

    fun dfs(idx: Int): Boolean {
        if (idx >= n - 1) return true
        val maxReach = minOf(idx + nums[idx], n - 1)
        return (idx + 1..maxReach).any { dfs(it) }
    }

    return dfs(0)
}


// ── 2. Top-down DP (memoized recursion) ─────────────────────
// Same recursion but cache each index's answer.
// T: O(n²)  S: O(n)
fun canJumpMemo(nums: IntArray): Boolean {
    val n = nums.size
    val memo = HashMap<Int, Boolean>()

    fun dfs(idx: Int): Boolean {
        if (idx >= n - 1) return true
        memo[idx]?.let { return it }
        val maxReach = minOf(idx + nums[idx], n - 1)
        val result = (idx + 1..maxReach).any { dfs(it) }
        return result.also { memo[idx] = it }
    }

    return dfs(0)
}


// ── 3. Bottom-up DP (tabulation) ────────────────────────────
// Fill dp right-to-left: dp[i] = can index i reach the end?
// T: O(n²)  S: O(n)
fun canJumpDP(nums: IntArray): Boolean {
    val n = nums.size
    val dp = BooleanArray(n) { false }
    dp[n - 1] = true // base: already at the end

    for (idx in n - 2 downTo 0) {
        val maxReach = minOf(idx + nums[idx], n - 1)
        dp[idx] = (idx + 1..maxReach).any { dp[it] }
    }

    return dp[0]
}


// ── 4. Greedy (optimal) ─────────────────────────────────────
// Track the farthest index reachable so far.
// If we ever stand on an index beyond that frontier, we're stuck.
// T: O(n)  S: O(1)
fun canJumpGreedy(nums: IntArray): Boolean {
    var maxReach = 0

    for (idx in nums.indices) {
        if (idx > maxReach) return false          // can't reach this index
        maxReach = maxOf(maxReach, idx + nums[idx])
    }

    return true
}


// ── 5. Greedy reverse (good-index propagation) ──────────────
// Walk right-to-left; shrink the "goal" whenever a position
// can directly reach it. If goal reaches 0, we win.
// T: O(n)  S: O(1)
fun canJumpGreedyReverse(nums: IntArray): Boolean {
    var goal = nums.size - 1

    for (idx in nums.size - 2 downTo 0) {
        if (idx + nums[idx] >= goal) goal = idx   // this index can reach the goal
    }

    return goal == 0
}
