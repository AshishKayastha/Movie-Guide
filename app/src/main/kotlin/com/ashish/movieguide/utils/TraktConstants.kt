package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig

object TraktConstants {

    const val TRAKT_API_BASE_URL = "https://api.trakt.tv/"

    const val TRAKT_CLIENT_ID = BuildConfig.TRAKT_CLIENT_ID
    const val TRAKT_CLIENT_SECRET = BuildConfig.TRAKT_CLIENT_SECRET

    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"

    const val API_VERSION = "2"
    const val HEADER_BEARER = "Bearer "
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_TRAKT_API_KEY = "trakt-api-key"
    const val HEADER_TRAKT_API_VERSION = "trakt-api-version"

    const val REDIRECT_URI = "mgoauth://logincallback"
    const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
}