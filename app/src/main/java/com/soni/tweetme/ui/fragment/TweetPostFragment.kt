package com.soni.tweetme.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.soni.tweetme.R
import com.soni.tweetme.databinding.FragmentTweetPostBinding
import com.soni.tweetme.network.response.Tweet
import com.soni.tweetme.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetPostFragment : Fragment() {
    val args: TweetPostFragmentArgs by navArgs()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private var imageUrl: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private lateinit var binding: FragmentTweetPostBinding

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
    }

    private fun initUI() {
        userId = args.userId
        userName = args.userName
        binding.progressLayout.setOnTouchListener { _, _ -> true }
    }

    private fun initListener() {
        binding.fabPhoto.setOnClickListener {
            addImage()
        }

        binding.tweetImage.setOnClickListener {
            addImage()
        }

        binding.fabSend.setOnClickListener {
            postTweet()
        }
    }

    // Add Tweet Image
    fun addImage() {
        /** The Intent refers to the intent action for picking the Photo from the device's storage */
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        // when starting for activityResult, override onActivityResult()
        startActivityForResult(intent, REQUEST_CODE_PHOTO)
    }

    /** Process onActivityResult */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            storeImage(data?.data)
        }
    }

    private fun storeImage(imageUri: Uri?) {
        imageUri?.let {
            binding.progressLayout.updateVisibility(true)

            // Create 'TweetImages' folder in STORAGE
            val filePath = firebaseStorage.child(DATA_TWEET_IMAGES).child(userId!!)

            // Save into Storage
            filePath.putFile(imageUri)
                .addOnSuccessListener {

                    // Get url from Storage >> DB
                    filePath.downloadUrl
                        .addOnSuccessListener { uri ->

                            imageUrl = uri.toString()
                            binding.tweetImage.loadUrl(imageUrl, R.drawable.logo)
                            binding.progressLayout.updateVisibility(false)
                        }
                        .addOnFailureListener {
                            onTweetImageFail()
                        }
                }
                .addOnFailureListener {
                    onTweetImageFail()
                }
        }
    }

    private fun onTweetImageFail() {
        Toast.makeText(requireContext(), "Image tweet failed. Try again..", Toast.LENGTH_SHORT)
            .show()
        binding.progressLayout.updateVisibility(false)
    }


    fun postTweet() {
        binding.progressLayout.updateVisibility(true)
        val text = binding.tweetText.text.toString()
        val hashtags = getHashtags(text)

        // POST: to update DB
        val tweetId = firebaseDB.collection(DATA_TWEETS).document() // Create Tweets Collection
        val tweet = Tweet(      // Tweet model
            tweetId.id,
            arrayListOf(userId!!),
            userName,
            text,
            imageUrl,
            System.currentTimeMillis(),
            hashtags,
            arrayListOf()
        )

        tweetId.set(tweet)
            .addOnCompleteListener { findNavController().popBackStack() }
            .addOnFailureListener {
                it.printStackTrace()
                binding.progressLayout.updateVisibility(false)
                Toast.makeText(requireContext(), "Failed to post tweet", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getHashtags(source: String): ArrayList<String> {
        val hashtags = arrayListOf<String>()
        var text = source   // assign source into variable

        while (text.contains("#")) {
            var hashtag = ""
            val hash = text.indexOf("#")
            text = text.substring(hash + 1)

            val firstSpace = text.indexOf(" ")
            val firstHash = text.indexOf("#")

            // Filter source of '#' and 'space'  -- Can use RegEx
            if (firstSpace == -1 && firstHash == -1) {
                hashtag = text.substring(0)
            } else if (firstSpace != -1 && firstSpace < firstHash) {
                hashtag = text.substring(0, firstSpace)
                text = text.substring(firstSpace + 1)
            } else {
                hashtag = text.substring(0, firstHash)
                text = text.substring(firstHash)
            }

            if (!hashtag.isNullOrEmpty()) {
                hashtags.add(hashtag)
            }
        }
        return hashtags
    }
}