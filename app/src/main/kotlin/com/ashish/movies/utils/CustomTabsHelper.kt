package com.ashish.movies.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.support.customtabs.CustomTabsIntent
import com.ashish.movies.R
import com.ashish.movies.utils.extensions.getColorCompat

/**
 * Created by Ashish on Jan 30.
 */
object CustomTabsHelper {

    private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE"

    fun launchUrl(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(context.getColorCompat(R.color.colorPrimary))
                .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
                .build()

        addKeepAliveExtra(context, customTabsIntent.intent)
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    fun addKeepAliveExtra(context: Context, intent: Intent) {
        val keepAliveIntent = Intent().setClassName(context.packageName, KeepAliveService::class.java.canonicalName)
        intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent)
    }
}

class KeepAliveService : Service() {

    companion object {
        private val sBinder = Binder()
    }

    override fun onBind(intent: Intent) = sBinder
}