package one528

class Solution {
    fun restoreString(s: String, indices: IntArray): String {
        val result = CharArray(s.length)

        for (i in s.indices) {
            result[indices[i]] = s[i]
        }

        return String(result)
    }
}
