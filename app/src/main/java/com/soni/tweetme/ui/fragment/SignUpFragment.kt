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
import com.google.firebase.firestore.FirebaseFirestore
import com.soni.tweetme.databinding.FragmentSignupBinding
import com.soni.tweetme.network.response.User
import com.soni.tweetme.ui.MainActivity
import com.soni.tweetme.utils.DATA_USERS
import com.soni.tweetme.utils.autoCleared
import com.soni.tweetme.utils.updateVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var binding by autoCleared<FragmentSignupBinding>()

    private val firebaseDB = FirebaseFirestore.getInstance()

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

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

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
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
            firebaseAuth.createUserWithEmailAndPassword(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Signup Error: ${it.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val email = binding.emailEditText.text.toString()
                        val username = binding.nameEditText.text.toString()
                        val user = User(email, username, "", arrayListOf(), arrayListOf())
                        firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                    }
                    binding.progressLayout.updateVisibility(false)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    binding.progressLayout.updateVisibility(false)
                }
        }
    }
}