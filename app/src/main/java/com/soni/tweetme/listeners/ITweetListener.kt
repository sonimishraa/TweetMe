package com.paolo_manlunas.twitterclone.listeners

import com.soni.tweetme.network.response.Tweet

interface ITweetListener {
    fun onDelete(tweet: Tweet?)
}