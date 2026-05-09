package seven71

class Solution {
    fun numJewelsInStones(jewels: String, stones: String): Int =
        stones.count { stones -> stones in jewels.toSet() }
}
