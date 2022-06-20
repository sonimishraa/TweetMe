package com.soni.tweetme.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.soni.tweetme.network.response.User
import com.soni.tweetme.utils.DATA_USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val application: Application,
) : ViewModel() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            isLoginSuccessMutableLiveData.postValue(true)
        }
    }

    val isLoginSuccessMutableLiveData = MutableLiveData(false)

    fun registerFirebaseAuth() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    fun unregisterFirebaseAuth() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    val networkStatusMutableLiveData = MutableLiveData<Boolean>()

    fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            Log.i("jaimatadi", "status = ${it.isSuccessful}")
            if (!it.isSuccessful) {
                networkStatusMutableLiveData.postValue(true)
            }
        }.addOnFailureListener {
            Log.i("jaimatadi", "status = error occur")
            it.printStackTrace()
            networkStatusMutableLiveData.postValue(true)
        }
    }

    val isPBStatusLiveData = MutableLiveData(false)
    val errorMsgLiveData = MutableLiveData<String>()

    fun createUserWithEmailAndPassword(email: String, password: String, userName: String) {
        firebaseAuth.createUserWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener {
                isPBStatusLiveData.postValue(true)
                if (!it.isSuccessful) {
                    errorMsgLiveData.postValue("Signup Error: ${it.exception?.localizedMessage}")
                } else {
                    val user = User(email, userName, "", arrayListOf(), arrayListOf())
                    firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                isPBStatusLiveData.postValue(true)
            }
    }
}