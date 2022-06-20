package com.soni.tweetme.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.paolo_manlunas.twitterclone.listeners.ITweetListener
import com.soni.tweetme.R
import com.soni.tweetme.databinding.ItemTweetBinding
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.utils.AppExecutors
import com.soni.tweetme.utils.getDate
import com.soni.tweetme.utils.loadUrl
import com.soni.tweetme.utils.updateVisibility

class TweetListAdapter(
    val userId: String,
    executors: AppExecutors
) : DataBoundListAdapter<Tweet, ItemTweetBinding>
    (appExecutors = executors, diffcallBack = object : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
        oldItem == newItem

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
        binding.tweetUsername.text = tweet.username
        binding.tweetText.text = tweet.text
        binding.tweetImage.updateVisibility(tweet.imageUrl.isNullOrEmpty().not())
        binding.tweetImage.loadUrl(tweet.imageUrl)

        binding.tweetDate.text = getDate(tweet.timestamp)
        binding.tweetLikeCount.text = tweet.likes?.size.toString()

        binding.tweetLayout.setOnClickListener { listenerI?.onLayoutClick(tweet) }
        binding.tweetLike.setOnClickListener { listenerI?.onLike(tweet) }
        binding.tweetDelete.setOnClickListener { listenerI?.onDelete(tweet) }

        if (tweet.likes?.contains(userId) == true) {
            binding.tweetLike.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.tweetLike.context,
                    R.drawable.like
                )
            )
        } else {
            binding.tweetLike.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.tweetLike.context,
                    R.drawable.like_inactive
                )
            )
        }
    }
}