package com.soni.tweetme.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.soni.tweetme.R
import java.text.DateFormat
import java.util.*

fun View.updateVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadUrl(url: String?, errorDrawable: Int = R.drawable.empty) {
    context?.let {
        val options = RequestOptions()
            .placeholder(progressDrawable(context))
            .error(errorDrawable)
        Glide.with(context.applicationContext)
            .load(url)
            .apply(options)
            .into(this)
    }
}

fun progressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
}

fun getDate(s: Long?): String {
    s?.let {
        val df = DateFormat.getDateInstance()
        return df.format(Date(it))
    }
    return "Unknown"
}