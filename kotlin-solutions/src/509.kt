package five09

class Solution {
    fun fib(n: Int): Int {
        if (n == 0) return 0
        if (n == 1) return 1
        // fib(0) =0 , f(1) = 1
        return (2..n).fold(0 to 1) { (minusTwo, minusOne), i ->
            // minusTwo to minusOne
            // n-2 to n-1
            minusOne to minusOne + minusTwo

        }.second


    }
}