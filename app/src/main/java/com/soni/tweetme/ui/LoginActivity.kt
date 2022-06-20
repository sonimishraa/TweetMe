package com.soni.tweetme.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.soni.tweetme.R
import com.soni.tweetme.databinding.ActivityLoginBinding
import com.soni.tweetme.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel>()
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
        viewModel.isLoginSuccessMutableLiveData.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.registerFirebaseAuth()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unregisterFirebaseAuth()
    }
}
