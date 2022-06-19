package com.soni.tweetme.utils

import android.view.View

fun View.updateVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}