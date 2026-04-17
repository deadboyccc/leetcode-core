package one523

class Solution {
    fun countOdds(low: Int, high: Int): Int =
        (low..high).count { it % 2 != 0 }


}