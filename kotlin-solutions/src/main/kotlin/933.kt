package nine33

/*
|--------------------------------------------------------------------------
| 1. Clean & Readable Version (Recommended)
|--------------------------------------------------------------------------
|
| Best balance between:
| - readability
| - interview clarity
| - maintainability
| - efficiency
|
| Easy to explain during interviews.
|
*/

class ReadableSolution {

    class RecentCounter {

        // Stores only valid recent ping times
        private val recentPings = ArrayDeque<Int>()

        /*
         * Valid time window:
         * [currentTime - 3000, currentTime]
         */
        fun ping(currentTime: Int): Int {

            // Add current request
            recentPings.addLast(currentTime)

            val intervalStart = currentTime - 3000

            /*
             * Remove outdated requests.
             *
             * Since requests arrive in increasing order,
             * the oldest request is always at the front.
             */
            while (recentPings.first() < intervalStart) {
                recentPings.removeFirst()
            }

            // Remaining requests are valid
            return recentPings.size
        }
    }
}

/*
|--------------------------------------------------------------------------
| 2. Minimal Concise Version
|--------------------------------------------------------------------------
|
| Same logic with minimal code.
|
| Good once you're already comfortable with the pattern.
|
*/

class ConciseSolution {

    class RecentCounter {

        private val window = ArrayDeque<Int>()

        fun ping(t: Int): Int {
            window.addLast(t)

            while (window.first() < t - 3000) {
                window.removeFirst()
            }

            return window.size
        }
    }
}

/*
|--------------------------------------------------------------------------
| 3. Idiomatic Kotlin Version (run scope function)
|--------------------------------------------------------------------------
|
| Most idiomatic Kotlin solution here.
|
| Why `run`?
| - operates on the deque
| - returns the final expression (`size`)
|
| Reads naturally:
| "operate on window then return size"
|
*/

class IdiomaticKotlinSolution {

    class RecentCounter {

        private val window = ArrayDeque<Int>()

        fun ping(t: Int): Int =
            window.run {

                // Add current request
                addLast(t)

                // Remove outdated requests
                while (first() < t - 3000) {
                    removeFirst()
                }

                // Return number of valid requests
                size
            }
    }
}

/*
|--------------------------------------------------------------------------
| 4. apply Scope Function Version
|--------------------------------------------------------------------------
|
| Uses `apply`.
|
| `apply` returns the object itself,
| so we access `.size` afterward.
|
| Slightly less semantically perfect than `run`,
| but still clean and idiomatic.
|
*/

class ApplyScopeFunctionSolution {

    class RecentCounter {

        private val window = ArrayDeque<Int>()

        fun ping(currentTime: Int): Int =
            window
                .apply {

                    addLast(currentTime)

                    while (first() < currentTime - 3000) {
                        removeFirst()
                    }
                }
                .size
    }
}

/*
|--------------------------------------------------------------------------
| 5. "More Functional" Sequence-Based Version
|--------------------------------------------------------------------------
|
| Tries to look more functional using:
| - generateSequence
| - takeIf
| - forEach
|
| Educational, but NOT recommended in practice.
|
| Downsides:
| - less readable
| - over-engineered
| - harder to understand quickly
|
| The simple while-loop is better here.
|
*/

class FunctionalStyleSolution {

    class RecentCounter {

        private val window = ArrayDeque<Int>()

        fun ping(currentTime: Int): Int =
            window.apply {

                // Add current request
                addLast(currentTime)

                /*
                 * Continuously generate invalid front values
                 * until no more outdated requests exist.
                 */
                generateSequence {
                    firstOrNull()
                        ?.takeIf { it < currentTime - 3000 }
                }.forEach {

                    removeFirst()
                }
            }.size
    }
}

/*
|--------------------------------------------------------------------------
| Core Intuition
|--------------------------------------------------------------------------
|
| We maintain a sliding window of valid requests.
|
| Valid interval:
|
|     [t - 3000, t]
|
| Since ping times arrive in increasing order:
|
| - newest requests go to the back
| - oldest requests stay at the front
|
| Therefore:
|
| 1. Add new request
| 2. Remove outdated requests from front
| 3. Remaining queue size = answer
|
|--------------------------------------------------------------------------
| Time Complexity
|--------------------------------------------------------------------------
|
| Amortized O(1)
|
| Each ping:
| - added once
| - removed once
|
|--------------------------------------------------------------------------
*/
