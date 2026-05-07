package nine33

class Readable {
    class RecentCounter {

        // Stores only recent ping times
        private val recentPings = ArrayDeque<Int>()

        fun ping(currentTime: Int): Int {

            // Add current request
            recentPings.addLast(currentTime)

            // Remove requests older than 3000 ms
            while (recentPings.first() < currentTime - 3000) {
                recentPings.removeFirst()
            }

            // Remaining requests are inside the valid window
            return recentPings.size
        }
    }
}

class RecentCounter() {
    private val window = ArrayDeque<Int>()

    fun ping(t: Int): Int {
        window.addLast(t)
        while (window.first() < t - 3000) {
            window.removeFirst()
        }
        return window.size
    }
}
