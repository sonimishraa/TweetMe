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
import com.soni.tweetme.databinding.FragmentSignupBinding
import com.soni.tweetme.utils.autoCleared
import com.soni.tweetme.utils.updateVisibility
import com.soni.tweetme.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var binding by autoCleared<FragmentSignupBinding>()
    private val viewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
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
        viewModel.isPBStatusLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressLayout.updateVisibility(!it)
            }
        }

        viewModel.errorMsgLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty().not()) {
                Toast.makeText(
                    requireContext(),
                    it,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initListener() {
        setTextChangeListener(binding.nameEditText, binding.nameLayout)
        setTextChangeListener(binding.emailEditText, binding.emailLayout)
        setTextChangeListener(binding.passwordEditText, binding.passwordLayout)

        binding.progressLayout.setOnTouchListener { v, event ->
            true
        }
        binding.buttonSignup.setOnClickListener {
            onSignup()
        }
        binding.signupTV.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setTextChangeListener(et: TextInputEditText, til: TextInputLayout) {
        et.addTextChangedListener {
            til.isErrorEnabled = false
        }
    }

    fun onSignup() {
        var proceed = true
        if (binding.nameEditText.text.isNullOrEmpty()) {
            binding.nameLayout.error = "Username is required"
            binding.nameLayout.isErrorEnabled = true
            proceed = false
        }

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
            viewModel.createUserWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.nameEditText.text.toString()
            )
        }
    }
}