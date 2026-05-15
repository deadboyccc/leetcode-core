package one5

class Solution {

    /**
     * APPROACH 1: HashSet Deduplication
     *
     * Intuition: Fix one element, run two-pointer 2Sum on the rest.
     *            Use a HashSet to silently discard duplicate triplets.
     *
     * Time:  O(n²) — O(n log n) sort + O(n²) two-pointer scan
     * Space: O(k)  — HashSet holds up to k unique triplets (k = output size)
     */
    fun threeSumHashSet(nums: IntArray): List<List<Int>> {
        var left = 0
        val sorted = nums.sorted()
        val res = hashSetOf<List<Int>>()

        while (left <= sorted.lastIndex) {
            val curNum = sorted[left]

            var low = left + 1
            var high = sorted.lastIndex

            while (low < high) {
                val lowNum = sorted[low]
                val highNum = sorted[high]
                val sum = lowNum + highNum + curNum
                if (sum == 0) res.add(listOf(curNum, lowNum, highNum))
                if (sum > 0) high-- else low++
            }

            left++
        }

        return res.toList()
    }

    /**
     * APPROACH 2: Explicit Duplicate Skipping (Optimal)
     *
     * Intuition: Fix one element, run two-pointer 2Sum on the rest.
     *            Instead of a HashSet, manually skip repeated values at
     *            each pointer so duplicates are never even considered.
     *            - Skip outer pointer  → avoids re-fixing same pivot
     *            - Skip inner pointers → avoids re-pairing same pair after a hit
     *            - Early break on sorted[i] > 0 → remaining pivots only grow
     *
     * Time:  O(n²) — O(n log n) sort + O(n²) two-pointer scan
     * Space: O(1)  — no auxiliary structure; only the output list
     */
    fun threeSum(nums: IntArray): List<List<Int>> {
        val sorted = nums.sorted()
        val result = mutableListOf<List<Int>>()

        for (i in 0..sorted.lastIndex - 2) {
            // Skip duplicate pivots (same value already explored in prior iteration)
            if (i > 0 && sorted[i] == sorted[i - 1]) continue

            // Sorted array: if smallest remaining element is positive, no triplet sums to 0
            if (sorted[i] > 0) break

            var low = i + 1
            var high = sorted.lastIndex

            while (low < high) {
                val sum = sorted[i] + sorted[low] + sorted[high]
                when {
                    sum == 0 -> {
                        result.add(listOf(sorted[i], sorted[low], sorted[high]))
                        // Skip duplicates for both inner pointers before advancing
                        while (low < high && sorted[low] == sorted[low + 1]) low++
                        while (low < high && sorted[high] == sorted[high - 1]) high--
                        low++; high--
                    }

                    sum < 0 -> low++    // Too small → move left pointer right
                    else -> high--   // Too large → move right pointer left
                }
            }
        }

        return result
    }
}