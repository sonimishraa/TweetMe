package com.soni.tweetme.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.paolo_manlunas.twitterclone.listeners.ITweetListener
import com.soni.tweetme.R
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.utils.getDate
import com.soni.tweetme.utils.loadUrl

class TweetListAdapter(val userId: String, val tweets: ArrayList<Tweet>) :
    RecyclerView.Adapter<TweetListAdapter.TweetViewHolder>() {
    private var listenerI: ITweetListener? = null

    fun setListener(listenerI: ITweetListener?) {
        this.listenerI = listenerI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TweetViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
    )

    override fun getItemCount() = tweets.size

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(userId, tweets[position], listenerI)
    }

    class TweetViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val layout = v.findViewById<ViewGroup>(R.id.tweetLayout)
        private val username = v.findViewById<TextView>(R.id.tweetUsername)
        private val text = v.findViewById<TextView>(R.id.tweetText)
        private val image = v.findViewById<ImageView>(R.id.tweetImage)
        private val date = v.findViewById<TextView>(R.id.tweetDate)
        private val like = v.findViewById<ImageView>(R.id.tweetLike)
        private val likeCount = v.findViewById<TextView>(R.id.tweetLikeCount)
        private val tweetDelete = v.findViewById<ImageView>(R.id.tweetDelete)

        fun bind(userId: String, tweet: Tweet, listenerI: ITweetListener?) {
            username.text = tweet.username
            text.text = tweet.text

            // tweeted Image
            if (tweet.imageUrl.isNullOrEmpty()) {
                image.visibility = View.GONE
            } else {
                image.visibility = View.VISIBLE
                image.loadUrl(tweet.imageUrl)
            }

            date.text = getDate(tweet.timestamp)
            likeCount.text = tweet.likes?.size.toString()

            // call listener when user Clicks on:
            layout.setOnClickListener { listenerI?.onLayoutClick(tweet) }
            like.setOnClickListener { listenerI?.onLike(tweet) }
            tweetDelete.setOnClickListener { listenerI?.onDelete(tweet) }

            // check if user already 'like' this tweet or not:
            if (tweet.likes?.contains(userId) == true) {
                like.setImageDrawable(ContextCompat.getDrawable(like.context, R.drawable.like))
            } else {
                like.setImageDrawable(
                    ContextCompat.getDrawable(
                        like.context,
                        R.drawable.like_inactive
                    )
                )
            }

            // check if user's original tweet, if not, if has already re-tweeted
            when {
                // userId[0] is the original tweeter userId
                tweet.userIds?.first().equals(userId) -> {
                    tweetDelete.setImageDrawable(
                        ContextCompat.getDrawable(
                            like.context,
                            R.drawable.delete_tweet
                        )
                    )
                    tweetDelete.isClickable = true
                }
            }
        }
    }

    fun updateTweets(newTweets: List<Tweet>) {
        tweets.clear()
        tweets.addAll(newTweets)
        notifyDataSetChanged()
    }
}