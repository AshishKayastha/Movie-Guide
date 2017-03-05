package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Ashish on Dec 28.
 */
class ApiKeyInterceptor @Inject constructor(
        private val preferenceHelper: PreferenceHelper
) : Interceptor {

    companion object {
        private const val API_KEY = "api_key"
        private const val LANGUAGE = "language"
        private const val US_ENGLISH = "en-US"
        private const val SESSION_ID = "session_id"
        private const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val httpUrlBuilder = originalRequest.url()
                    .newBuilder()
                    .addQueryParameter(API_KEY, TMDB_API_KEY)
                    .addQueryParameter(LANGUAGE, US_ENGLISH)

            val sessionId = preferenceHelper.getSessionId()
            if (sessionId.isNotNullOrEmpty()) {
                httpUrlBuilder.addQueryParameter(SESSION_ID, sessionId)
            }

            val newRequest = originalRequest.newBuilder()
                    .url(httpUrlBuilder.build())
                    .build()

            return chain.proceed(newRequest)
        }

        return null
    }
}