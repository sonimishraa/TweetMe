package com.soni.tweetme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.paolo_manlunas.twitterclone.listeners.IHomeCallback
import com.paolo_manlunas.twitterclone.listeners.TwitterListenerImpl
import com.soni.tweetme.R
import com.soni.tweetme.databinding.FragmentMainBinding
import com.soni.tweetme.ui.adapters.TweetListAdapter
import com.soni.tweetme.utils.*
import com.soni.tweetme.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var binding by autoCleared<FragmentMainBinding>()
    lateinit var tweetListAdapter: TweetListAdapter
    private val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListener()
        initObserve()
    }

    private fun initUI() {
        tweetListAdapter = TweetListAdapter(appExecutors)
        tweetListAdapter.setListener(
            TwitterListenerImpl(
                binding.tweetList,
                viewModel.currentUser,
                object : IHomeCallback {
                    override fun onUserUpdated() {
                        populate()
                    }

                    override fun onRefresh() {
                        viewModel.updateList()
                    }
                })
        )

        binding.tweetList.apply {
            adapter = tweetListAdapter
            setHasFixedSize(true)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.updateList()
        }

        viewModel.updateList()
    }

    private fun initListener() {
        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.actionTweet, bundleOf(
                    PARAM_USER_ID to viewModel.userId,
                    (PARAM_USER_NAME to viewModel.user?.displayName.orEmpty())
                )
            )
        }
    }

    private fun initObserve() {
        viewModel.isPBStausMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressLayout.updateVisibility(!it)
            }
        }

        viewModel.sortedListMutableLiveData.observe(viewLifecycleOwner) {
            if (::tweetListAdapter.isInitialized) {
                tweetListAdapter.submitList(it)
            }
        }
    }

    private fun populate() {
        binding.progressLayout.updateVisibility(true)
        viewModel.getList()
    }
}