package com.soni.tweetme.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.soni.tweetme.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val application: Application,
    private val myRepository: TweetRepository,
) : ViewModel() {

}