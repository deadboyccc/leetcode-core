package one464;

class SolutionFP {
    fun maxProduct(nums: IntArray): Int {

        val (highest, secondHighest) =
            nums.fold(0 to 0) { (highest, secondHighest), current ->
                when {
                    current > highest -> current to highest
                    current > secondHighest -> highest to current
                    else -> highest to secondHighest
                }
            }

        return (highest - 1) * (secondHighest - 1)
    }
}

class Solution {
    fun maxProduct(nums: IntArray): Int {
        var max1 = 0
        var max2 = 0

        for (num in nums) {
            if (num > max1) {
                // The old max1 is now the second largest
                max2 = max1
                max1 = num
            } else if (num > max2) {
                // num is between max1 and max2
                max2 = num
            }
        }

        return (max1 - 1) * (max2 - 1)
    }
}

class SolutionReadable {
    fun maxProduct(nums: IntArray): Int {
        var highest = 0
        var secondHighest = 0

        for (currentValue in nums) {
            if (currentValue > highest) {
                // If we find a new champion, the old champion
                // drops down to take the silver medal.
                secondHighest = highest
                highest = currentValue
            } else if (currentValue > secondHighest) {
                // If it's not the champion, it might still beat
                // the current silver medalist.
                secondHighest = currentValue
            }
        }

        return (highest - 1) * (secondHighest - 1)
    }
}