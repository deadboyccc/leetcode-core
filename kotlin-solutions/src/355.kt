package three55

class Twitter() {
    var time = 0

    // @ManyToMany
    // map each userId to a list of its followers
    val followers = mutableMapOf<Int, MutableList<Int>>()

    // map each userId to a list of its tweets
    val tweets = mutableMapOf<Int, MutableList<Int>>()

    // everything is sequential
    // maps tweetId -> sequence
    val tweetDate = mutableMapOf<Int, Int>()



    fun postTweet(userId: Int, tweetId: Int) {
        tweets.getOrDefault(userId, mutableListOf()).add(tweetId)
        tweetDate[tweetId] = ++time

    }

    fun getNewsFeed(userId: Int): List<Int> {
        // TODO


    }

    fun follow(followerId: Int, followeeId: Int) {
        // followers -> followee
        followers.getOrDefault(followeeId, mutableListOf()).add(followerId)

    }

    fun unfollow(followerId: Int, followeeId: Int) {

        followers.getOrDefault(followeeId, mutableListOf()).remove(followerId)

    }

}

/**
 * Your Twitter object will be instantiated and called as such:
 * var obj = Twitter()
 * obj.postTweet(userId,tweetId)
 * var param_2 = obj.getNewsFeed(userId)
 * obj.follow(followerId,followeeId)
 * obj.unfollow(followerId,followeeId)
 */