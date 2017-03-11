package com.ashish.movieguide.utils

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection

@Singleton
class OfflineCacheInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        var request = chain.request()
        if (!Utils.isOnline()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }

        val response = chain.proceed(request)

        // If 504 is thrown then there's no cached response so throw NoCacheException
        if (response != null && response.code() == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT) {
            throw NoCacheException()
        }

        return response
    }
}