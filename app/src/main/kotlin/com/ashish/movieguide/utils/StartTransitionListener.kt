package com.ashish.movieguide.utils

import android.app.Activity
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception

class StartTransitionListener<R>(private val activity: Activity) : RequestListener<String, R> {

    override fun onException(e: Exception?, model: String?, target: Target<R>?,
                             isFirstResource: Boolean): Boolean {
        activity.startPostponedEnterTransition()
        return false
    }

    override fun onResourceReady(resource: R?, model: String?, target: Target<R>?,
                                 isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        activity.startPostponedEnterTransition()
        return false
    }
}