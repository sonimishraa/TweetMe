package com.soni.tweetme.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBoundViewHolder<T : ViewDataBinding> constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root) {
}