package com.soni.tweetme.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.network.response.User
import com.soni.tweetme.utils.DATA_TWEETS
import com.soni.tweetme.utils.DATA_TWEET_IMAGES
import com.soni.tweetme.utils.DATA_TWEET_USER_IDS
import com.soni.tweetme.utils.DATA_USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val application: Application,
) : ViewModel() {

    var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    val tweetImageFailMutableLiveData = MutableLiveData(false)
    val tweetImageURIMutableLiveData = MutableLiveData<String>()

    private val firebaseStorage = FirebaseStorage.getInstance().reference

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

    val tweetStatusMutableLiveData = MutableLiveData<String>()

    fun postTweet(text: String, userName: String?, imageUrl: String?) {
        val hashtags = getHashtags(text)
        // POST: to update DB
        val tweetId = firebaseDB.collection(DATA_TWEETS).document() // Create Tweets Collection
        val tweet = Tweet(      // Tweet model
            tweetId.id,
            arrayListOf(userId),
            userName,
            text,
            imageUrl,
            System.currentTimeMillis(),
            hashtags,
            arrayListOf()
        )

        tweetId.set(tweet)
            .addOnCompleteListener {
                tweetStatusMutableLiveData.postValue("SUCCESS")
            }
            .addOnFailureListener {
                it.printStackTrace()
                tweetStatusMutableLiveData.postValue("FAIL")
            }
    }

    fun getHashtags(source: String): ArrayList<String> {
        val hashtags = arrayListOf<String>()
        var text = source   // assign source into variable

        while (text.contains("#")) {
            var hashtag = ""
            val hash = text.indexOf("#")
            text = text.substring(hash + 1)

            val firstSpace = text.indexOf(" ")
            val firstHash = text.indexOf("#")

            // Filter source of '#' and 'space'  -- Can use RegEx
            if (firstSpace == -1 && firstHash == -1) {
                hashtag = text.substring(0)
            } else if (firstSpace != -1 && firstSpace < firstHash) {
                hashtag = text.substring(0, firstSpace)
                text = text.substring(firstSpace + 1)
            } else {
                hashtag = text.substring(0, firstHash)
                text = text.substring(firstHash)
            }

            if (!hashtag.isNullOrEmpty()) {
                hashtags.add(hashtag)
            }
        }
        return hashtags
    }

    fun storeImage(imageUri: Uri) {
        val filePath = firebaseStorage.child(DATA_TWEET_IMAGES).child(userId)
        // Save into Storage
        filePath.putFile(imageUri)
            .addOnSuccessListener {

                // Get url from Storage >> DB
                filePath.downloadUrl
                    .addOnSuccessListener { uri ->
                        tweetImageURIMutableLiveData.postValue(uri.toString())
                    }
                    .addOnFailureListener {
                        tweetImageFailMutableLiveData.postValue(true)
                    }
            }
            .addOnFailureListener {
                tweetImageFailMutableLiveData.postValue(true)
            }
    }
}