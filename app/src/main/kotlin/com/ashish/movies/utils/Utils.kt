package com.ashish.movies.utils

import android.content.res.Resources
import com.ashish.movies.app.MoviesApp
import com.ashish.movies.utils.extensions.connectivityManager

/**
 * Created by Ashish on Dec 30.
 */
object Utils {

    fun isOnline(): Boolean {
        val activeNetworkInfo = MoviesApp.context.connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels
}