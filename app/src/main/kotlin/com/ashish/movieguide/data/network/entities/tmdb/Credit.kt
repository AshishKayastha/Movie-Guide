package com.ashish.movieguide.data.network.entities.tmdb

import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 03.
 */
@JsonClass(generateAdapter = true)
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
) : RecyclerViewItem {

    override fun getViewType() = CONTENT_VIEW
}