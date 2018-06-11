package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcelable
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashish on Dec 27.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
        val id: Long? = null,
        val title: String? = null,
        val overview: String? = null,
        val rating: Double? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null
) : RecyclerViewItem, Parcelable {

    override fun getViewType() = CONTENT_VIEW
}