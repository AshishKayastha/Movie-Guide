package com.ashish.movieguide.utils.glide

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber

class LoggingListener<T> : RequestListener<T> {

    override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
    ): Boolean {
        Timber.e(e)
        return false
    }

    override fun onResourceReady(
            resource: T?,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
    ): Boolean {
        Timber.v("onResourceReady Called")
        return false
    }
}