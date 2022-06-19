package com.soni.tweetme.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.soni.tweetme.R
import com.soni.tweetme.databinding.ActivityLoginBinding
import com.soni.tweetme.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initObserve()
    }

    private fun initUI() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    private fun initObserve() {
    }
}