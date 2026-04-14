package two68

class Solution {
    fun missingNumber(nums: IntArray): Int {
        val set = nums.toHashSet()
        return (0..nums.size).filter { it !in set }.first()
    }
}

fun main() {
    val (left, right) = listOf(1, 2, 3, 4, 5, 6).partition { it > 3 }
    val splitIndex = left.size
    println("left $left ----split index : $splitIndex------ right $right")

}