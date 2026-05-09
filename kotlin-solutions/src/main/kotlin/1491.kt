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

class SolutionFP {
    fun average(salary: IntArray): Double =
    // Triple of [ Sum - max - in ] -> for each salary sum+= salary, max = maxOf, min = minOf
        // let ( map ) -> sum max min -> sum-max-min / salary.size -2
        salary.fold(Triple(0.0, Int.MIN_VALUE, Int.MAX_VALUE)) { (sum, max, min), s ->
            Triple(sum + s, maxOf(max, s), minOf(min, s))
        }.let { (sum, max, min) -> (sum - max - min) / (salary.size - 2) }

}