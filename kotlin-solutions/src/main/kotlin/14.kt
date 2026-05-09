package one4;

class Solution {
    fun longestCommonPrefix(strs: Array<String>): String {
        if (strs.isEmpty()) return ""
        strs.sort()

        val firstStr = strs[0]
        val lastStr = strs[strs.lastIndex]

        val length = minOf(firstStr.length, lastStr.length)

        return buildString {


            for (i in 0 until length) {
                if (firstStr[i] != lastStr[i]) {
                    break
                }
                append(firstStr[i])


            }
        }


    }
}