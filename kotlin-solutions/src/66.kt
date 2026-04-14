package six6;

class SolutionFP {
    fun plusOne(digits: IntArray): IntArray =
        digits.foldRight(1 to emptyList<Int>()) { digit, (carry, acc) ->
            val sum = digit + carry
            (sum / 10) to (listOf(sum % 10) + acc)
        }.let { (carry, result) ->
            (if (carry > 0) listOf(carry) + result else result).toIntArray()
        }
}

class Solution {
    fun plusOne(digits: IntArray): IntArray {
        val list = digits.toMutableList()

        for (i in list.indices.reversed()) {
            if (list[i] == 9) {
                list[i] = 0
            } else {
                list[i] = list[i] + 1
                return list.toIntArray()   // done -> no carry propagates further
            }
        }

        // All digits were 9 → all zeroed out, prepend the carry
        list.add(0, 1)
        return list.toIntArray()
    }
}
