package com.paolo_manlunas.twitterclone.listeners

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.network.response.User
import com.soni.tweetme.utils.DATA_TWEETS
import com.soni.tweetme.utils.DATA_TWEET_USER_IDS

class TwitterListenerImpl(
    val tweetList: RecyclerView,
    var user: User?,
    val callback: IHomeCallback?
) : ITweetListener {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onDelete(tweet: Tweet?) {
        tweet?.let {
            tweetList.isClickable = false
            val userIds = it.userIds
            userIds?.remove(userId)
            // UPDATE Db
            firebaseDB.collection(DATA_TWEETS).document(it.tweetId!!)
                .update(DATA_TWEET_USER_IDS, userIds)
                .addOnSuccessListener {
                    tweetList.isClickable = true
                    callback?.onRefresh()
                }
                .addOnFailureListener {
                    tweetList.isClickable = true
                }
        }
    }
}