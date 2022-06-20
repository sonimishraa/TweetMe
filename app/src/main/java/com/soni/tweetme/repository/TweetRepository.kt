package com.soni.tweetme.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class TweetRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth
) {
    fun signOut() {
        firebaseAuth.signOut()
    }
}