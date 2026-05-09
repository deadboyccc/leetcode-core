package six57

class Solution {
    fun judgeCircle(moves: String): Boolean {
        var x = 0
        var y = 0
        for (move in moves) {
            when (move) {
                'L' -> x--
                'R' -> x++
                'U' -> y++
                'D' -> y--
            }
        }
        return x == 0 && y == 0
    }
}