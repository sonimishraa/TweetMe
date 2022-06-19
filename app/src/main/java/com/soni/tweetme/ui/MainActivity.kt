package com.soni.tweetme.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.soni.tweetme.R
import com.soni.tweetme.databinding.ActivityMainBinding
import com.soni.tweetme.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initListener()
        initObserve()
    }

    private fun initUI() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun initListener() {
    }

    private fun initObserve() {
    }
}
