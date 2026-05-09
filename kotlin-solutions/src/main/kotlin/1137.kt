package one137;

class OptimizedSolution {
    fun tribonacci(n: Int): Int {
        if (n == 0) return 0
        if (n == 1 || n == 2) return 1

        var minusThree = 0
        var minusTwo = 1
        var minusOne = 1

        for (i in 3..n) {
            val currTrib = minusThree + minusTwo + minusOne

            // Shift the values forward
            minusThree = minusTwo
            minusTwo = minusOne
            minusOne = currTrib
        }

        return minusOne
    }
}

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