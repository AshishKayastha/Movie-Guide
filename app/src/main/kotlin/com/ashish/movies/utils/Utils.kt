package com.ashish.movies.utils

import android.app.Service
import android.net.ConnectivityManager
import com.ashish.movies.app.MoviesApp

/**
 * Created by Ashish on Dec 30.
 */
object Utils {

    fun isOnline(): Boolean {
        val cm = MoviesApp.context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}