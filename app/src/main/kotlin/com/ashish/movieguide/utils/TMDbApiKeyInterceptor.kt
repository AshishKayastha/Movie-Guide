package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 28.
 */
@Singleton
class TMDbApiKeyInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val US_ENGLISH = "en-US"
        private const val HEADER_API_KEY = "api_key"
        private const val HEADER_LANGUAGE = "language"
        private const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val httpUrlBuilder = originalRequest.url()
                    .newBuilder()
                    .addQueryParameter(HEADER_API_KEY, TMDB_API_KEY)
                    .addQueryParameter(HEADER_LANGUAGE, US_ENGLISH)

            val newRequest = originalRequest.newBuilder()
                    .url(httpUrlBuilder.build())
                    .build()

            return chain.proceed(newRequest)
        }

        return null
    }
}