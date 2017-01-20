package com.ashish.movies.data.models

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

@PaperParcel
data class TVShow(
        val id: Long? = null,
        val name: String? = null,
        val overview: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "first_air_date") val firstAirDate: String? = null
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelTVShow.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelTVShow.writeToParcel(this, dest, flags)
    }
}