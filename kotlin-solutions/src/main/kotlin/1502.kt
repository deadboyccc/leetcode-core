package one502;

class Solution {
    fun canMakeArithmeticProgression(arr: IntArray): Boolean {
        // 1. Sort the array to put numbers in sequence
        arr.sort()

        // 2. Calculate the common difference from the first two elements
        val diff = arr[1] - arr[0]

        // 3. Check if every consecutive pair has the same difference
        // We start from index 1 and look back to avoid bounds issues
        for (i in 2 until arr.size) {
            if (arr[i] - arr[i - 1] != diff) {
                return false
            }
        }

        return true
    }
}

class SolutionFP {
    fun canMakeArithmeticProgression(arr: IntArray): Boolean {
        arr.sort()
        val diff = arr[1] - arr[0]
        return (1 until arr.lastIndex).all { i ->
            arr[i + 1] - arr[i] == diff
        }
    }
}

class SolutionFPPlus {
    fun canMakeArithmeticProgression(arr: IntArray): Boolean {
        arr.sort()
        val diff = arr[1] - arr[0]

        // 1. Initial value must be true
        // 2. We only iterate up to lastIndex - 1 to prevent IndexOutOfBounds
        return arr.indices.drop(1).fold(true) { acc, index ->
            acc && (arr[index + 1] - arr[index] == diff)
        }
    }
}
