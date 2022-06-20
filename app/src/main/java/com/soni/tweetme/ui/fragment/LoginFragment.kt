package com.soni.tweetme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.soni.tweetme.R
import com.soni.tweetme.databinding.FragmentLoginBinding
import com.soni.tweetme.utils.autoCleared
import com.soni.tweetme.utils.updateVisibility
import com.soni.tweetme.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var binding by autoCleared<FragmentLoginBinding>()
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
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
    }

    private fun initObserver() {
        viewModel.networkStatusMutableLiveData.observe(viewLifecycleOwner) {
            if (true) {
                binding.progressLayout.updateVisibility(false)
                Toast.makeText(
                    requireContext(), "Login Error:", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initListener() {
        setTextChangeListener(binding.emailEditText, binding.emailLayout)
        setTextChangeListener(binding.passwordEditText, binding.passwordLayout)
        binding.progressLayout.setOnTouchListener { v, event ->
            true
        }
        binding.login.setOnClickListener {
            onLogin()
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.actionSignUp)
        }
    }

    fun setTextChangeListener(et: TextInputEditText, til: TextInputLayout) {
        et.addTextChangedListener {
            til.isErrorEnabled = false
        }
    }

    fun onLogin() {
        var proceed = true

        if (binding.emailEditText.text.isNullOrEmpty()) {
            binding.emailLayout.error = "Email is required"
            binding.emailLayout.isErrorEnabled = true
            proceed = false
        }

        if (binding.passwordEditText.text.isNullOrEmpty()) {
            binding.passwordLayout.error = "Password is required"
            binding.passwordLayout.isErrorEnabled = true
            proceed = false
        }

        if (proceed) {
            binding.progressLayout.updateVisibility(true)
            viewModel.signInWithEmailAndPassword(
                binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()
            )
        }
    }
}