package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

@PaperParcel
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
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelEpisode.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelEpisode.writeToParcel(this, dest, flags)
    }
}