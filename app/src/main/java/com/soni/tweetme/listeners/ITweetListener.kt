package com.paolo_manlunas.twitterclone.listeners

import com.soni.tweetme.network.response.Tweet

interface ITweetListener {
    fun onLayoutClick(tweet: Tweet?)
    fun onLike(tweet: Tweet?)
    fun onRetweet(tweet: Tweet?)
}