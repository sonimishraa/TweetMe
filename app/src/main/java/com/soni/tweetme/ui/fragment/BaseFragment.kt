package com.soni.tweetme.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.paolo_manlunas.twitterclone.listeners.IHomeCallback
import com.paolo_manlunas.twitterclone.listeners.TwitterListenerImpl
import com.soni.tweetme.network.response.User
import com.soni.tweetme.ui.adapters.TweetListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    protected var tweetsAdapter: TweetListAdapter? = null
    protected var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    protected val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    protected var listenerI: TwitterListenerImpl? = null
    protected var callback: IHomeCallback? = null

    // Instantiate Interface callback for use by other classes
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IHomeCallback) {
            callback =
                context  // Attaches the Interface to the current context(Activity) which it's implemented
        } else {
//            throw RuntimeException("$context must implement IHomeCallback")
        }
    }

    fun setUser(user: User?) {
        this.currentUser = user
        listenerI?.user = user
    }

    abstract fun updateList()

    override fun onResume() {
        super.onResume()
        updateList()
    }
}