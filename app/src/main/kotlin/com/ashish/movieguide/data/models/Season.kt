package com.ashish.movieguide.data.models

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

@PaperParcel
data class Season(
        val id: Long? = null,
        @Json(name = "air_date") val airDate: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "episode_count") val episodeCount: Int? = null,
        @Json(name = "season_number") val seasonNumber: Int? = null
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelSeason.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelSeason.writeToParcel(this, dest, flags)
    }
}