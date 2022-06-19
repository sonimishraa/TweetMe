package com.soni.tweetme.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.paolo_manlunas.twitterclone.listeners.IHomeCallback
import com.paolo_manlunas.twitterclone.listeners.TwitterListenerImpl
import com.soni.tweetme.databinding.FragmentMainBinding
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.ui.adapters.TweetListAdapter
import com.soni.tweetme.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private var binding by autoCleared<FragmentMainBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
        initListener()
    }

    private fun initUI() {
        val tweetListAdapter = TweetListAdapter(userId, arrayListOf())
        tweetListAdapter.setListener(
            TwitterListenerImpl(
                binding.tweetList,
                currentUser,
                object : IHomeCallback {
                    override fun onUserUpdated() {
                        populate()
                    }

                    override fun onRefresh() {
                        updateList()
                    }
                })
        )
        binding.tweetList.apply {
            adapter = tweetListAdapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            updateList()
        }
    }

    private fun initObserver() {
    }

    private fun initListener() {
    }

    /** FROM: TwitterFragment abstract method */
    override fun updateList() {
        binding.tweetList.updateVisibility(false)
        val tweets = arrayListOf<Tweet>()
        firebaseDB.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_USER_IDS, userId!!).get()
            .addOnSuccessListener { list ->
                Log.i("jaimatadi", "list documents = ${list.documents.size}")
                for (document in list.documents) {
                    val tweet = document.toObject(Tweet::class.java)
                    tweet?.let { tweets.add(tweet) }    // add to tweets arrayList
                }
                val sortedList = tweets.sortedWith(compareByDescending { it.timestamp })
                Log.i("jaimatadi", "sortedlist = ${sortedList.size}")
                tweetsAdapter?.updateTweets(sortedList)
                binding.tweetList.updateVisibility(true)
            }
            .addOnFailureListener {
                Log.i("jaimatadi", "error occurred")
                it.printStackTrace()
                binding.tweetList.updateVisibility(true)
            }
    }

    private fun populate() {
        binding.progressLayout.updateVisibility(true)
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                binding.progressLayout.updateVisibility(false)
                updateList()
            }
            .addOnFailureListener {
                it.printStackTrace()
                requireActivity().finish()
            }
    }
}