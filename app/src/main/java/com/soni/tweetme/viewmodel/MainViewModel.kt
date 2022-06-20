package com.soni.tweetme.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.soni.tweetme.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val application: Application,
    private val myRepository: TweetRepository,
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
        }
    }

    fun registerFirebaseAuth() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    fun unregisterFirebaseAuth() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    val networkStatusMutableLiveData = MutableLiveData<Boolean>()
}