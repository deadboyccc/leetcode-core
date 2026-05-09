package three660

// claude
class Solution {
    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val parent = IntArray(n) { it }
        val compMax = nums.copyOf()

        fun find(x: Int): Int {
            if (parent[x] != x) parent[x] = find(parent[x])
            return parent[x]
        }

        fun union(a: Int, b: Int) {
            val ra = find(a);
            val rb = find(b)
            if (ra == rb) return
            parent[rb] = ra
            compMax[ra] = maxOf(compMax[ra], compMax[rb])
        }

        // (index, effectiveMax) — effectiveMax = max value in this component
        val stack = ArrayDeque<Pair<Int, Int>>()

        for (j in 0 until n) {
            var curMax = nums[j]
            while (stack.isNotEmpty() && stack.last().second > nums[j]) {
                val (idx, effMax) = stack.removeLast()
                union(idx, j)
                curMax = maxOf(curMax, effMax)
            }
            stack.addLast(j to curMax)
        }

        return IntArray(n) { compMax[find(it)] }
    }
}

class Editorial2Solution {

    data class Item(
        var value: Int,
        var left: Int,
        var right: Int
    )

    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)

        val stack = mutableListOf<Item>()

        for (i in nums.indices) {

            val current = Item(
                value = nums[i],
                left = i,
                right = i
            )

            while (
                stack.isNotEmpty() &&
                stack.last().value > nums[i]
            ) {
                val top = stack.removeAt(stack.lastIndex)

                current.value = maxOf(current.value, top.value)
                current.left = top.left
            }

            stack.add(current)
        }

        for (item in stack) {
            for (index in item.left..item.right) {
                answer[index] = item.value
            }
        }

        return answer
    }
}

class EditorialSolution {

    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)

        // prevMax[i] = Pair(maxValueSoFar, indexOfThatMax)
        val prevMax = Array(n) { 0 to 0 }

        var currentMax = Int.MIN_VALUE
        var currentMaxIndex = -1

        for (i in nums.indices) {
            if (nums[i] > currentMax) {
                currentMax = nums[i]
                currentMaxIndex = i
            }

            prevMax[i] = currentMax to currentMaxIndex
        }

        fun process(
            right: Int,
            rightMin: Int,
            rightMax: Int
        ) {
            val (prefixMax, pivotIndex) = prevMax[right]

            val currentAnswer =
                if (prefixMax <= rightMin) prefixMax
                else rightMax

            var nextRightMin = minOf(prefixMax, rightMin)

            for (i in pivotIndex..right) {
                answer[i] = currentAnswer
                nextRightMin = minOf(nextRightMin, nums[i])
            }

            if (pivotIndex == 0) {
                return
            }

            process(
                pivotIndex - 1,
                nextRightMin,
                currentAnswer
            )
        }

        process(
            right = n - 1,
            rightMin = Int.MAX_VALUE,
            rightMax = 0
        )

        return answer
    }
}