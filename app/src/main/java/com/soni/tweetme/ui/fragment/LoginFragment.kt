package com.soni.tweetme.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.soni.tweetme.R
import com.soni.tweetme.databinding.FragmentLoginBinding
import com.soni.tweetme.ui.MainActivity
import com.soni.tweetme.utils.autoCleared
import com.soni.tweetme.utils.updateVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var binding by autoCleared<FragmentLoginBinding>()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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


    /* when LoginActivity starts up, we have to attach this to FirebaseAuth instance
*  to check if user is already logged-in.
* */
    override fun onStart() {
        super.onStart()

        // pass the firebaseAuthListener to see if user is still logged-in
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
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
            firebaseAuth.signInWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        binding.progressLayout.updateVisibility(false)
                        Toast.makeText(
                            requireContext(),
                            "Login Error: ${it.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    binding.progressLayout.updateVisibility(false)
                }
        }
    }
}