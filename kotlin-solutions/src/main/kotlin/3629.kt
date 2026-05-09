package three629


class Solution {
    fun minJumps(nums: IntArray): Int {
        if (nums.isEmpty()) return 0
        var count = 0
        var ptr = 0
        while (ptr < nums.lastIndex) {
            // todo

        }


        return count


    }

    fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        if (n <= 3) return true

        // eliminate multiples of 2 and 3 early
        if (n % 2 == 0 || n % 3 == 0) return false

        var i = 5
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) return false
            i += 6
        }

        return true
    }
}