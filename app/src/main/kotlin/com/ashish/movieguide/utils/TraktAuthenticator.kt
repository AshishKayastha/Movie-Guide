package com.ashish.movieguide.utils

import com.ashish.movieguide.data.network.entities.trakt.TraktToken
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.qualifiers.BaseOkHttp
import com.ashish.movieguide.utils.TraktConstants.GRANT_TYPE_REFRESH_TOKEN
import com.ashish.movieguide.utils.TraktConstants.HEADER_AUTHORIZATION
import com.ashish.movieguide.utils.TraktConstants.HEADER_BEARER
import com.ashish.movieguide.utils.TraktConstants.REDIRECT_URI
import com.ashish.movieguide.utils.TraktConstants.TRAKT_API_BASE_URL
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_ID
import com.ashish.movieguide.utils.TraktConstants.TRAKT_CLIENT_SECRET
import com.squareup.moshi.Moshi
import okhttp3.Authenticator
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraktAuthenticator @Inject constructor(
        private val moshi: Moshi,
        @BaseOkHttp private val okHttpClient: OkHttpClient,
        private val preferenceHelper: PreferenceHelper
) : Authenticator {

    companion object {
        private const val REFRESH_TOKEN_URL = TRAKT_API_BASE_URL + "oauth/token"
    }

    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        if (TRAKT_API_BASE_URL != response.request().url().host()) {
            return null
        }

        if (responseCount(response) >= 3) return null

        val refreshToken = preferenceHelper.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return null

        // Try to refresh the access token with the refresh token
        val refreshResponse = refreshToken(refreshToken!!)
        if (!refreshResponse.isSuccessful) return null

        val jsonAdapter = moshi.adapter(TraktToken::class.java)
        val traktToken = jsonAdapter.fromJson(refreshResponse.body().string())

        val accessToken = traktToken.accessToken
        preferenceHelper.setAccessToken(accessToken)
        preferenceHelper.setRefreshToken(traktToken.refreshToken)

        return response.request().newBuilder()
                .header(HEADER_AUTHORIZATION, HEADER_BEARER + accessToken)
                .build()
    }

    private fun refreshToken(refreshToken: String): Response {
        val formBody = FormBody.Builder()
                .add("refresh_token", refreshToken)
                .add("client_id", TRAKT_CLIENT_ID)
                .add("client_secret", TRAKT_CLIENT_SECRET)
                .add("redirect_uri", REDIRECT_URI)
                .add("grant_type", GRANT_TYPE_REFRESH_TOKEN)
                .build()

        val request = Request.Builder()
                .url(REFRESH_TOKEN_URL)
                .post(formBody)
                .build()

        return okHttpClient.newCall(request).execute()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        val priorResponse = response.priorResponse()
        while (priorResponse != null) {
            result++
        }
        return result
    }
}