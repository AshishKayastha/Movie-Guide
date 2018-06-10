package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcelable
import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem
import com.ashish.movieguide.ui.common.adapter.RecyclerViewItem.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Episode(
        val id: Long? = null,
        val name: String? = null,
        val overview: String? = null,
        val rating: Double? = null,
        @Json(name = "show_id") val tvShowId: Long? = null,
        @Json(name = "air_date") val airDate: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "still_path") val stillPath: String? = null,
        @Json(name = "season_number") val seasonNumber: Int? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "episode_number") val episodeNumber: Int? = null
) : RecyclerViewItem, Parcelable {

    override fun getViewType() = CONTENT_VIEW
}