package com.ashish.movieguide.utils

import android.content.res.Resources
import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.utils.extensions.connectivityManager

/**
 * Created by Ashish on Dec 30.
 */
object Utils {

    fun isOnline(): Boolean {
        val activeNetworkInfo = MovieGuideApp.context.connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels
}