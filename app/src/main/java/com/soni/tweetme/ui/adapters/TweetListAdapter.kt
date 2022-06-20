package com.soni.tweetme.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.paolo_manlunas.twitterclone.listeners.ITweetListener
import com.soni.tweetme.databinding.ItemTweetBinding
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.utils.AppExecutors
import com.soni.tweetme.utils.getDate
import com.soni.tweetme.utils.loadUrl
import com.soni.tweetme.utils.updateVisibility

class TweetListAdapter(
    executors: AppExecutors
) : DataBoundListAdapter<Tweet, ItemTweetBinding>
    (appExecutors = executors, diffcallBack = object : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
        oldItem.tweetId == newItem.tweetId

    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
        oldItem == newItem

}) {
    override fun createBinding(parent: ViewGroup): ItemTweetBinding =
        ItemTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    private var listenerI: ITweetListener? = null
    fun setListener(listenerI: ITweetListener?) {
        this.listenerI = listenerI
    }

    override fun bind(
        binding: ItemTweetBinding,
        tweet: Tweet,
        isLast: Boolean
    ) {
        binding.item = tweet
        binding.tweetImage.updateVisibility(tweet.imageUrl.isNullOrEmpty().not())
        binding.tweetImage.loadUrl(tweet.imageUrl)
        binding.tweetDate.text = getDate(tweet.timestamp)
        binding.tweetDelete.setOnClickListener { listenerI?.onDelete(tweet) }
    }
}