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


    /* when LoginActivity starts up, we have to attach this to FirebaseAuth instance
*  to check if user is already logged-in.
* */
    override fun onStart() {
        super.onStart()
        // pass the firebaseAuthListener to see if user is still logged-in
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }


    /* when screen starts up, we have to detach/remove  this from FirebaseAuth */
    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }


    fun onSignup() {
        var proceed = true

        // Username Check
        if (binding.nameEditText.text.isNullOrEmpty()) {
            binding.nameLayout.error = "Username is required"
            binding.nameLayout.isErrorEnabled = true
            proceed = false
        }

        // Email Check
        if (binding.emailEditText.text.isNullOrEmpty()) {
            binding.emailLayout.error = "Email is required"
            binding.emailLayout.isErrorEnabled = true
            proceed = false
        }

        // Password Check
        if (binding.passwordEditText.text.isNullOrEmpty()) {
            binding.passwordLayout.error = "Password is required"
            binding.passwordLayout.isErrorEnabled = true
            proceed = false
        }

        // if Signup proceeds
        if (proceed) {
            binding.progressLayout.updateVisibility(true)

            // Signup with Firebase Auth
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
                        /* Create User in FirebaseDB */
                        val email = binding.emailEditText.text.toString()
                        val username = binding.nameEditText.text.toString()

                        // User data class Object (from util)
                        val user = User(email, username, "", arrayListOf(), arrayListOf())

                        /* Add 'user' Object to DB:
                        *  firebaseAuth.uid!! connects the the Auth user and corresponding DB with UID.
                        *  - Create Collection with DATA_USERS constant
                        *  - set() will Save 'user' object in Collection.
                        * */
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