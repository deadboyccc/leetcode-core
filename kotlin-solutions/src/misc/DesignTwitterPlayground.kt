package misc.systemDesign.twitter

import java.util.*

// practical per LeetCode constraint of small users + tweets size
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
            // get all the relevant ids
            feedSources(userId)
                // get their tweet lists and flatten them into one list
                .flatMap { tweets[it] ?: emptyList() }
                // sort the result by time (newest first)
                .sortedByDescending { it.time }
                // take the first 10
                .take(10)
                // map each Tweet it's its id
                .map { it.id }
        // we get a list of ids

        fun follow(followerId: Int, followeeId: Int) {
            if (followerId != followeeId)
                following.getOrPut(followerId) { mutableSetOf() }.add(followeeId)
        }

        fun unfollow(followerId: Int, followeeId: Int) {
            following[followerId]?.remove(followeeId)
        }

        private fun feedSources(userId: Int) = (following[userId] ?: emptySet()) + userId
    }
}

// Best Solution - scales well
class Twitter {
    // tweet data: ID for identification, time for chronological sorting
    private data class Tweet(val id: Int, val time: Int)

    // heap pointer: stores current tweet, the author, and position in author's list
    private data class HeapEntry(val tweet: Tweet, val userId: Int, val index: Int)

    private var timestamp = 0
    private val tweets = mutableMapOf<Int, MutableList<Tweet>>() // userId -> list of their tweets
    private val following = mutableMapOf<Int, MutableSet<Int>>() // userId -> set of IDs they follow

    /** Adds a new tweet: O(1) time */
    fun postTweet(userId: Int, tweetId: Int) {
        // Increment global time to ensure unique ordering
        tweets.getOrPut(userId) { mutableListOf() }.add(Tweet(tweetId, ++timestamp))
    }

    /**
     * K-Way Merge: Fetches 10 most recent tweets from user + followees.
     * Time: O(K log N) where K is 10 and N is number of followees.
     */
    fun getNewsFeed(userId: Int): List<Int> {
        // Max-heap to keep the most recent tweet (highest time) at the top
        val maxHeap = PriorityQueue<HeapEntry>(compareByDescending { it.tweet.time })

        // Initial Step: Push the very last (newest) tweet from every feed source into heap
        for (sourceId in feedSources(userId)) {
            val userTweets = tweets[sourceId] ?: continue
            val lastIdx = userTweets.lastIndex
            maxHeap.offer(HeapEntry(userTweets[lastIdx], sourceId, lastIdx))
        }

        return buildList {
            // Extraction Step: Pull the top of heap, then "move pointer back" to that user's previous tweet
            while (maxHeap.isNotEmpty() && size < 10) {
                val (tweet, sourceId, idx) = maxHeap.poll()
                add(tweet.id)

                // If this user has an older tweet, add it to heap as a new candidate
                if (idx > 0) {
                    val prevTweet = tweets[sourceId]!![idx - 1]
                    maxHeap.offer(HeapEntry(prevTweet, sourceId, idx - 1))
                }
            }
        }
    }

    /** Follows a user: O(1) time */
    fun follow(followerId: Int, followeeId: Int) {
        if (followerId != followeeId)
            following.getOrPut(followerId) { mutableSetOf() }.add(followeeId)
    }

    /** Unfollows a user: O(1) time */
    fun unfollow(followerId: Int, followeeId: Int) {
        following[followerId]?.remove(followeeId)
    }

    /** Internal helper: Returns set of IDs (Followed users + Self) */
    private fun feedSources(userId: Int) = (following[userId] ?: emptySet()) + userId
}
