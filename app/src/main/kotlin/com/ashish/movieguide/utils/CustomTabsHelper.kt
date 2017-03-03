package com.ashish.movieguide.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.support.customtabs.CustomTabsIntent
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.getColorCompat

/**
 * Created by Ashish on Jan 30.
 */
object CustomTabsHelper {

    const val RC_TMDB_LOGIN = 1001
    private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE"

    fun launchUrlForResult(activity: Activity, url: String, requestCode: Int = RC_TMDB_LOGIN) {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(activity.getColorCompat(R.color.colorPrimary))
                .setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(activity, R.anim.slide_in_left, R.anim.slide_out_right)
                .build()

        val intent = customTabsIntent.intent

        addKeepAliveExtra(activity, intent)
        intent.data = Uri.parse(url)
        activity.startActivityForResult(intent, requestCode)
    }

    fun addKeepAliveExtra(context: Context, intent: Intent) {
        val keepAliveIntent = Intent().setClassName(context.packageName, KeepAliveService::class.java.canonicalName)
        intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent)
    }
}

class KeepAliveService : Service() {

    companion object {
        private val binder = Binder()
    }

    override fun onBind(intent: Intent) = binder
}