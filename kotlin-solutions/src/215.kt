package two15

class Solution {
    fun findKthLargest(nums: IntArray, k: Int): Int {
        var l = 0
        var r = nums.lastIndex
        val target = k - 1

        while (l < r) {
            val pivotIndex = partition(nums, l, r)

            // In Hoare partition, the split point 'p' guarantees:
            // nums[l..p] contains elements >= pivot
            // nums[p+1..r] contains elements <= pivot
            if (target <= pivotIndex) {
                r = pivotIndex
            } else {
                l = pivotIndex + 1
            }
        }
        return nums[l]
    }

    private fun partition(nums: IntArray, l: Int, r: Int): Int {
        // Using the middle element as the pivot
        val pivot = nums[l + (r - l) / 2]
        var i = l - 1
        var j = r + 1

        while (true) {
            // Move i to the right as long as nums[i] > pivot (for descending)
            do {
                i++
            } while (nums[i] > pivot)

            // Move j to the left as long as nums[j] < pivot (for descending)
            do {
                j--
            } while (nums[j] < pivot)

            // If pointers cross, the partition is complete
            if (i >= j) return j

            // Swap elements using .also
            nums[i] = nums[j].also { nums[j] = nums[i] }
        }
    }
}
