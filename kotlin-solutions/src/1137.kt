package one137;

class Solution {
    val memo = mutableMapOf<Int, Int>()
    fun tribonacci(n: Int): Int {
        // base cases
        if (n == 0 || n == 1) return n
        if (n == 2) return 1

        // memo
        // t(n) = t(n-1) + t(n-2) + t(n-3)

        return memo.getOrPut(n, { tribonacci(n - 1) + tribonacci(n - 2) + tribonacci(n - 3) })


    }
}