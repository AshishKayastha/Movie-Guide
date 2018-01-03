package com.ashish.movieguide.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.support.customtabs.CustomTabsIntent
import com.ashish.movieguide.R
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty

/**
 * Created by Ashish on Jan 30.
 */
object CustomTabsHelper {

    private const val RC_TMDB_LOGIN = 1001
    private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE"

    fun launchUrl(activity: Activity, url: String?) {
        if (url.isNotNullOrEmpty()) {
            val intent = buildCustomTabsIntent(activity)
            intent.data = Uri.parse(url)
            activity.startActivity(intent)
        }
    }

    fun launchUrlForResult(activity: Activity, url: String?, requestCode: Int = RC_TMDB_LOGIN) {
        if (url.isNotNullOrEmpty()) {
            val intent = buildCustomTabsIntent(activity)
            addKeepAliveExtra(activity, intent)
            intent.data = Uri.parse(url)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private fun buildCustomTabsIntent(activity: Activity): Intent {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(activity.getColorCompat(R.color.colorPrimary))
                .setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(activity, R.anim.slide_in_left, R.anim.slide_out_right)
                .build()

        return customTabsIntent.intent
    }

    private fun addKeepAliveExtra(context: Context, intent: Intent) {
        val keepAliveIntent = Intent().setClassName(context.packageName, KeepAliveService::class.java.canonicalName)
        intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent)
    }
}

@SuppressLint("Registered")
class KeepAliveService : Service() {

    companion object {
        private val BINDER = Binder()
    }

    override fun onBind(intent: Intent) = BINDER
}