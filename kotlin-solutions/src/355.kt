import java.util.*

class Twitter() {
    private var timestamp = 0

    // User ID -> The list of tweets they've posted (Newest at the end)
    private val tweetMap = mutableMapOf<Int, MutableList<Tweet>>()

    // User ID -> The set of people they ARE FOLLOWING
    private val followingMap = mutableMapOf<Int, MutableSet<Int>>()

    data class Tweet(val id: Int, val time: Int)

    /** Helper to get followings and include the user themselves */
    private fun getFeedSources(userId: Int): Set<Int> {
        return (followingMap[userId] ?: mutableSetOf()) + userId
    }

    fun postTweet(userId: Int, tweetId: Int) {
        tweetMap.getOrPut(userId) { mutableListOf() }.add(Tweet(tweetId, ++timestamp))
    }

    /**
     * Retrieves the 10 most recent tweet IDs in the user's news feed.
     * Uses a Max-Heap to merge multiple sorted tweet lists (K-way merge).
     */
    fun getNewsFeed(userId: Int): List<Int> {
        // PriorityQueue stores: Triple(Tweet, UserID, IndexOfTweetInUsersList)
        // We want a Max-Heap based on tweet.time
        val maxHeap = PriorityQueue<Triple<Tweet, Int, Int>>(compareByDescending { it.first.time })

        // Initialize the heap with the LATEST tweet from each source (the last item in their list)
        for (sourceId in getFeedSources(userId)) {
            val tweets = tweetMap[sourceId]
            if (!tweets.isNullOrEmpty()) {
                val lastIdx = tweets.lastIndex
                maxHeap.offer(Triple(tweets[lastIdx], sourceId, lastIdx))
            }
        }

        val feed = mutableListOf<Int>()
        while (maxHeap.isNotEmpty() && feed.size < 10) {
            val (tweet, sourceId, index) = maxHeap.poll()
            feed.add(tweet.id)

            // If this user has more (older) tweets, add the next one to the heap
            if (index > 0) {
                val nextIdx = index - 1
                val nextTweet = tweetMap[sourceId]!![nextIdx]
                maxHeap.offer(Triple(nextTweet, sourceId, nextIdx))
            }
        }
        return feed
    }

    fun follow(followerId: Int, followeeId: Int) {
        if (followerId != followeeId) {
            followingMap.getOrPut(followerId) { mutableSetOf() }.add(followeeId)
        }
    }

    fun unfollow(followerId: Int, followeeId: Int) {
        followingMap[followerId]?.remove(followeeId)
    }
}

class Optimized {

    class Twitter {
        private var timestamp = 0
        private val tweets = mutableMapOf<Int, MutableList<Tweet>>()
        private val following = mutableMapOf<Int, MutableSet<Int>>()

        private data class Tweet(val id: Int, val time: Int)
        private data class HeapEntry(val tweet: Tweet, val userId: Int, val index: Int)

        fun postTweet(userId: Int, tweetId: Int) {
            tweets.getOrPut(userId) { mutableListOf() }.add(Tweet(tweetId, ++timestamp))
        }

        // K-way merge across all feed sources via max-heap on tweet time
        fun getNewsFeed(userId: Int): List<Int> {
            val maxHeap = PriorityQueue<HeapEntry>(compareByDescending { it.tweet.time })

            for (sourceId in feedSources(userId)) {
                val userTweets = tweets[sourceId] ?: continue
                maxHeap.offer(HeapEntry(userTweets[userTweets.lastIndex], sourceId, userTweets.lastIndex))
            }

            return buildList {
                while (maxHeap.isNotEmpty() && size < 10) {
                    val (tweet, sourceId, idx) = maxHeap.poll()
                    add(tweet.id)
                    if (idx > 0) maxHeap.offer(HeapEntry(tweets[sourceId]!![idx - 1], sourceId, idx - 1))
                }
            }
        }

        fun follow(followerId: Int, followeeId: Int) {
            if (followerId != followeeId)
                following.getOrPut(followerId) { mutableSetOf() }.add(followeeId)
        }

        fun unfollow(followerId: Int, followeeId: Int) {
            following[followerId]?.remove(followeeId)
        }

        // includes the user themselves as an implicit followee
        private fun feedSources(userId: Int) = (following[userId] ?: emptySet<Int>()) + userId
    }
}

class OptimizedOnLeetCodeConstraints {
    class Twitter {
        private var timestamp = 0
        private val tweets = mutableMapOf<Int, MutableList<Tweet>>()
        private val following = mutableMapOf<Int, MutableSet<Int>>()

        private data class Tweet(val id: Int, val time: Int)

        fun postTweet(userId: Int, tweetId: Int) {
            tweets.getOrPut(userId) { mutableListOf() }.add(Tweet(tweetId, ++timestamp))
        }

        fun getNewsFeed(userId: Int): List<Int> =
            feedSources(userId)
                .flatMap { tweets[it] ?: emptyList() }
                .sortedByDescending { it.time }
                .take(10)
                .map { it.id }

        fun follow(followerId: Int, followeeId: Int) {
            if (followerId != followeeId)
                following.getOrPut(followerId) { mutableSetOf() }.add(followeeId)
        }

        fun unfollow(followerId: Int, followeeId: Int) {
            following[followerId]?.remove(followeeId)
        }

        private fun feedSources(userId: Int) = (following[userId] ?: emptySet<Int>()) + userId
    }
}