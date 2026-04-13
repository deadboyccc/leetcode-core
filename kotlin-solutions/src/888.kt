package eight88

class Solution {
    fun fairCandySwap(aliceSizes: IntArray, bobSizes: IntArray): IntArray {
        val diff = (aliceSizes.sum() - bobSizes.sum()) / 2

        val bobSet = bobSizes.toHashSet()

        for (aliceCard in aliceSizes) {
            val targetBobCard = aliceCard - diff
            if (bobSet.contains(targetBobCard)) {
                return intArrayOf(aliceCard, targetBobCard)
            }
        }

        return intArrayOf()
    }
}
