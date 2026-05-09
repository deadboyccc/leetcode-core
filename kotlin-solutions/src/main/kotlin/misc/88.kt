package misc.eight8

class Solution {
    fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int): Unit {
        var newArr = listOf<Int>()
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (nums1[i] < nums2[j]) {
                    newArr += nums1[i]
                } else {
                    newArr += nums2[j]
                }
            }

        }


    }
}

