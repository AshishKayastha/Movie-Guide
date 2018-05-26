package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import paperparcel.PaperParcel

@PaperParcel
@JsonClass(generateAdapter = true)
data class TVShow(
        val id: Long? = null,
        val name: String? = null,
        val overview: String? = null,
        val rating: Double? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "first_air_date") val firstAirDate: String? = null
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = PaperParcelTVShow.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelTVShow.writeToParcel(this, dest, flags)
    }
}