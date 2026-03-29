package misc.leetcode;

class Solution {
    fun isPalindrome(x: Int): Boolean = x.toString() == x.toString().reversed()
}

fun main() {
    val solution = Solution().isPalindrome(121)
    println(solution)

}
