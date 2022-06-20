package com.soni.tweetme.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.network.response.User
import com.soni.tweetme.repository.TweetRepository
import com.soni.tweetme.utils.DATA_TWEETS
import com.soni.tweetme.utils.DATA_TWEET_USER_IDS
import com.soni.tweetme.utils.DATA_USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val application: Application,
    private val myRepository: TweetRepository,
) : ViewModel() {
    var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    val isPBStausMutableLiveData = MutableLiveData(false)

    fun getList() {
        firebaseDB.collection(DATA_USERS).document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                isPBStausMutableLiveData.postValue(true)
                updateList()
            }
            .addOnFailureListener {
                it.printStackTrace()
                isPBStausMutableLiveData.postValue(true)
            }
    }

    val sortedListMutableLiveData = MutableLiveData<List<Tweet>>()

    fun updateList() {
        val tweets = arrayListOf<Tweet>()
        firebaseDB.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_USER_IDS, userId).get()
            .addOnSuccessListener { list ->
                Log.i("jaimatadi", "list documents = ${list.documents.size}")
                for (document in list.documents) {
                    val tweet = document.toObject(Tweet::class.java)
                    tweet?.let { tweets.add(tweet) }
                }
                val sortedList = tweets.sortedWith(compareByDescending { it.timestamp })
                Log.i("jaimatadi", "sortedlist = ${sortedList.size}")
                sortedListMutableLiveData.postValue(sortedList)
            }
            .addOnFailureListener {
                Log.i("jaimatadi", "error occurred")
                it.printStackTrace()
            }
    }
}