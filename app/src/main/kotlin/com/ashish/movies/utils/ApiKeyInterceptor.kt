package com.ashish.movies.utils

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Ashish on Dec 28.
 */
class ApiKeyInterceptor : Interceptor {

    companion object {
        const val API_KEY = "api_key"
        const val LANGUAGE = "language"
        const val TMDB_API_KEY = "21962412093fb3887ace2f97f2253eae"
    }

    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val httpUrlBuilder = originalRequest.url().newBuilder()
            httpUrlBuilder.addQueryParameter(API_KEY, TMDB_API_KEY)
            httpUrlBuilder.addQueryParameter(LANGUAGE, "en-US")

            val newRequest = originalRequest.newBuilder()
                    .url(httpUrlBuilder.build())
                    .build()

            return chain.proceed(newRequest)
        }

        return null
    }
}