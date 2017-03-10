package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig.TRAKT_CLIENT_ID
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraktApiInterceptor @Inject constructor(
        private val preferenceHelper: PreferenceHelper
) : Interceptor {

    companion object {
        private const val BEARER = "Bearer "
        private const val TRAKT_API_VERSION_2 = "2"
        private const val AUTHORIZATION = "Authorization"
        private const val TRAKT_API_KEY = "trakt-api-key"
        private const val TRAKT_API_VERSION = "trakt-api-version"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder()
                    .header(TRAKT_API_VERSION, TRAKT_API_VERSION_2)
                    .header(TRAKT_API_KEY, TRAKT_CLIENT_ID)

            val accessToken = preferenceHelper.getAccessToken()
            if (accessToken.isNotNullOrEmpty()) {
                builder.addHeader(AUTHORIZATION, BEARER + accessToken)
            }

            val newRequest = builder.build()
            return chain.proceed(newRequest)
        }

        return null
    }
}