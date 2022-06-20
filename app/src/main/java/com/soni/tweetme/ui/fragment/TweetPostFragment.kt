package com.soni.tweetme.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.soni.tweetme.R
import com.soni.tweetme.databinding.FragmentTweetPostBinding
import com.soni.tweetme.utils.loadUrl
import com.soni.tweetme.utils.updateVisibility
import com.soni.tweetme.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetPostFragment : Fragment() {
    val args: TweetPostFragmentArgs by navArgs()
    private var imageUrl: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private lateinit var binding: FragmentTweetPostBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTweetPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListener()
        initObserve()
    }

    private fun initObserve() {
        viewModel.tweetStatusMutableLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "SUCCESS" -> {
                    findNavController().popBackStack()
                }
                "FAIL" -> {
                    binding.progressLayout.updateVisibility(false)
                    Toast.makeText(requireContext(), "Failed to post tweet", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {}
            }
        }

        viewModel.tweetImageFailMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Image tweet failed. Try again..",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.progressLayout.updateVisibility(false)
            }
        }

        viewModel.tweetImageURIMutableLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty().not()) {
                imageUrl = it
                binding.tweetImage.loadUrl(imageUrl, R.drawable.logo)
                binding.progressLayout.updateVisibility(false)
            }
        }
    }

    private fun initUI() {
        userId = args.userId
        userName = args.userName
        binding.progressLayout.setOnTouchListener { _, _ -> true }
    }

    private fun initListener() {
        binding.fabPhoto.setOnClickListener {
            launchImagePickIntent()
        }

        binding.tweetImage.setOnClickListener {
            launchImagePickIntent()
        }

        binding.fabSend.setOnClickListener {
            postTweet()
        }

        binding.tweetText.addTextChangedListener {
            binding.warningMsg.text = "${it.toString().length}/150"
        }
    }

    private fun storeImage(imageUri: Uri?) {
        imageUri?.let {
            binding.progressLayout.updateVisibility(true)
            viewModel.storeImage(it)
        }
    }

    fun postTweet() {
        binding.progressLayout.updateVisibility(true)
        val text = binding.tweetText.text.toString()
        viewModel.postTweet(text, userName, imageUrl)
    }

    private fun launchImagePickIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startForResult.launch(intent)
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                storeImage(result.data?.data)
            } else Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT)
                .show()
        }
}