package com.ashish.movieguide.utils

import android.app.Activity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class StartTransitionListener<T>(private val activity: Activity) : RequestListener<T> {

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<T>?,
                              isFirstResource: Boolean): Boolean {
        activity.startPostponedEnterTransition()
        return false
    }

    override fun onResourceReady(resource: T?, model: Any?, target: Target<T>?, dataSource: DataSource?,
                                 isFirstResource: Boolean): Boolean {
        activity.startPostponedEnterTransition()
        return false
    }
}