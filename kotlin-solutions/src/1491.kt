package one491

class Solution {
    fun average(salary: IntArray): Double {
        var sum = 0.0
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE

        for (s in salary) {
            sum += s
            max = maxOf(max, s)
            min = minOf(min, s)
        }

        return (sum - max - min) / (salary.size - 2)
    }
}