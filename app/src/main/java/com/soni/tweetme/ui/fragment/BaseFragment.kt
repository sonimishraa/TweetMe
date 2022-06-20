package com.soni.tweetme.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.paolo_manlunas.twitterclone.listeners.IHomeCallback
import com.paolo_manlunas.twitterclone.listeners.TwitterListenerImpl
import com.soni.tweetme.network.response.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    protected var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    protected val user = FirebaseAuth.getInstance().currentUser
    protected val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    protected var callback: IHomeCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IHomeCallback) {
            callback =
                context
        }
    }

    abstract fun updateList()

    override fun onResume() {
        super.onResume()
        updateList()
    }
}