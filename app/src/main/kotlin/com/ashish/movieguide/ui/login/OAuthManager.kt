package com.ashish.movieguide.ui.login

import android.app.Activity
import com.ashish.movieguide.utils.Constants.TRAKT_CLIENT_ID
import com.ashish.movieguide.utils.CustomTabsHelper
import javax.inject.Inject

class OAuthManager @Inject constructor(private val activity: Activity) {

    fun launchOAuthUrl() {
        val url = "https://trakt.tv/oauth/authorize?" +
                "response_type=code" +
                "&client_id=" + TRAKT_CLIENT_ID +
                "&redirect_uri=" + ""

        CustomTabsHelper.launchUrlForResult(activity, url, 1002)
    }

    fun handleOAuthResult() {

    }
}