package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig.TRAKT_CLIENT_ID
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.utils.TraktConstants.API_VERSION
import com.ashish.movieguide.utils.TraktConstants.CONTENT_TYPE_JSON
import com.ashish.movieguide.utils.TraktConstants.HEADER_AUTHORIZATION
import com.ashish.movieguide.utils.TraktConstants.HEADER_BEARER
import com.ashish.movieguide.utils.TraktConstants.HEADER_CONTENT_TYPE
import com.ashish.movieguide.utils.TraktConstants.HEADER_TRAKT_API_KEY
import com.ashish.movieguide.utils.TraktConstants.HEADER_TRAKT_API_VERSION
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Traktnterceptor @Inject constructor(
        private val preferenceHelper: PreferenceHelper
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder()
                    .header(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                    .header(HEADER_TRAKT_API_VERSION, API_VERSION)
                    .header(HEADER_TRAKT_API_KEY, TRAKT_CLIENT_ID)

            val accessToken = preferenceHelper.getAccessToken()
            if (accessToken.isNotNullOrEmpty()) {
                builder.header(HEADER_AUTHORIZATION, HEADER_BEARER + accessToken)
            }

            // Enable Cache
            val response = chain.proceed(builder.build())
            return response.newBuilder()
                    .header("Cache-Control", "max-age=" + 120)
                    .build()
        }

        return null
    }
}