package three49

class Solution {
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        val set = nums1.toHashSet()
        val result = mutableListOf<Int>()
        nums2.forEach {
            if (it in set) {
                result.add(it)
                set.remove(it)
            }
        }
        return result.toIntArray()

    }
}

class SolutionFP {
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        return (nums1.toSet() intersect nums2.toSet()).toIntArray()
    }
}

class SolutionFPMinus {
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        val set = nums1.toHashSet()
        return nums2.distinct().filter { it in set }.toIntArray()
    }

}