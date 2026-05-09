// optimal uses % but i wanna try dp
package two92

class Solution {
    private val memo = mutableMapOf<Int, Boolean>()

    fun canWinNim(n: Int): Boolean {
        if (n <= 3) return true

        // Check cache to avoid redundant calculations
        memo[n]?.let { return it }

        /* * Logic: If I take 1, 2, or 3 stones, my opponent starts their turn
         * with (n-1), (n-2), or (n-3) stones.
         * If they lose (!) any of those scenarios, then I win.
         */
        val result = !canWinNim(n - 1) || !canWinNim(n - 2) || !canWinNim(n - 3)

        memo[n] = result
        return result
    }
}
