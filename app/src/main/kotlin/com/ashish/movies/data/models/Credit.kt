package com.ashish.movies.data.models

import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json

/**
 * Created by Ashish on Jan 03.
 */
data class Credit(
        val id: Long? = null,
        val name: String? = null,
        val title: String? = null,
        val job: String? = null,
        val character: String? = null,
        @Json(name = "media_type") val mediaType: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "profile_path") val profilePath: String? = null
) : ViewType {

    override fun getViewType() = CONTENT_VIEW

    override fun getContentId() = id
}