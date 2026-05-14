package three660

class MonotonicStack {

    /*
     * Each connected component becomes one continuous segment.
     *
     * Everybody inside a component can eventually reach
     * the component's maximum value.
     *
     * So the problem becomes:
     *
     * "Build connected components and assign their max."
     */

    data class Component(
        var maxValue: Int,
        var left: Int,
        var right: Int
    )

    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)

        /*
         * Stack stores connected components.
         *
         * Components are kept in increasing order of maxValue.
         *
         * Example:
         *
         * [max=3] [max=7] [max=12]
         */
        val stack = ArrayDeque<Component>()

        for (i in nums.indices) {

            /*
             * Start as its own component.
             */
            val current = Component(
                maxValue = nums[i],
                left = i,
                right = i
            )

            /*
             * If previous component's maximum
             * is bigger than current number:
             *
             * previousMax > nums[i]
             *
             * then they connect.
             *
             * Once connected,
             * components merge into one larger component.
             */
            while (
                stack.isNotEmpty() &&
                stack.last().maxValue > nums[i]
            ) {
                val previous = stack.removeLast()

                /*
                 * Merged component keeps:
                 *
                 * - biggest maximum
                 * - leftmost boundary
                 */
                current.maxValue =
                    maxOf(current.maxValue, previous.maxValue)

                current.left = previous.left
            }

            /*
             * Extend merged component's right boundary.
             */
            current.right = i

            stack.addLast(current)
        }

        /*
         * Every index inside a component
         * gets the component maximum.
         */
        for (component in stack) {
            for (index in component.left..component.right) {
                answer[index] = component.maxValue
            }
        }

        return answer
    }
}

class DivideAndConquer {
    fun maxValue(nums: IntArray): IntArray {
        val n = nums.size
        val answer = IntArray(n)

        /*
         * prefixMax[i] =
         *   biggest value in nums[0..i]
         *
         * prefixMaxIndex[i] =
         *   where that biggest value lives
         *
         * Example:
         * nums = [7,2,5,1,9]
         *
         * prefixMax      = [7,7,7,7,9]
         * prefixMaxIndex = [0,0,0,0,4]
         */
        val prefixMax = IntArray(n)
        val prefixMaxIndex = IntArray(n)

        var currentMax = Int.MIN_VALUE
        var currentMaxIndex = -1

        for (i in nums.indices) {
            if (nums[i] > currentMax) {
                currentMax = nums[i]
                currentMaxIndex = i
            }

            prefixMax[i] = currentMax
            prefixMaxIndex[i] = currentMaxIndex
        }

        /*
         * Processes interval [0..right]
         *
         * rightMin:
         *   smallest value in the already-solved region on the right
         *
         * rightAnswer:
         *   maximum value reachable in the already-solved region
         *
         * Core idea:
         *
         * If current prefix maximum > rightMin,
         * then this interval can connect to the solved right side,
         * therefore everybody upgrades to rightAnswer.
         *
         * Otherwise this interval stays isolated,
         * and everybody's answer becomes its own prefix maximum.
         */
        fun solve(
            right: Int,
            rightMin: Int,
            rightAnswer: Int
        ) {
            if (right < 0) return

            // Biggest value inside current interval [0..right]
            val currentMax = prefixMax[right]

            // Where that maximum lives
            val pivot = prefixMaxIndex[right]

            /*
             * Current chunk is [pivot..right]
             *
             * Example:
             *
             * [7,2,5,1,9,4,3]
             *          ^
             *        pivot=4
             *
             * chunk = [9,4,3]
             */

            val canReachRightSide = currentMax > rightMin

            // Final answer for this chunk
            val chunkAnswer =
                if (canReachRightSide) rightAnswer
                else currentMax

            /*
             * Update minimum reachable value.
             *
             * This becomes the new "bridge"
             * for intervals further left.
             */
            var nextRightMin = rightMin

            for (i in pivot..right) {
                answer[i] = chunkAnswer
                nextRightMin = minOf(nextRightMin, nums[i])
            }

            /*
             * Recurse on remaining left side:
             *
             * [0..pivot-1]
             */
            solve(
                right = pivot - 1,
                rightMin = nextRightMin,
                rightAnswer = chunkAnswer
            )
        }

        /*
         * Initially:
         *
         * nothing exists on the right,
         * so:
         *
         * rightMin = +infinity
         *
         * and the first chunk keeps its own maximum.
         */
        solve(
            right = n - 1,
            rightMin = Int.MAX_VALUE,
            rightAnswer = prefixMax[n - 1]
        )

        return answer
    }
}
