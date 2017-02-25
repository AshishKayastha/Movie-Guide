package com.ashish.movies.utils

import com.ashish.movies.utils.Constants.API_KEY
import com.ashish.movies.utils.Constants.LANGUAGE
import com.ashish.movies.utils.Constants.TMDB_API_KEY
import com.ashish.movies.utils.Constants.US_ENGLISH
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Ashish on Dec 28.
 */
class ApiKeyInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            val originalRequest = chain.request()

            val httpUrlBuilder = originalRequest.url()
                    .newBuilder()
                    .addQueryParameter(API_KEY, TMDB_API_KEY)
                    .addQueryParameter(LANGUAGE, US_ENGLISH)

            val newRequest = originalRequest.newBuilder()
                    .url(httpUrlBuilder.build())
                    .build()

            return chain.proceed(newRequest)
        }

        return null
    }
}