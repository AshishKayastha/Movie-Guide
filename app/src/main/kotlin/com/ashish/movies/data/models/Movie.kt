package com.ashish.movies.data.models

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

/**
 * Created by Ashish on Dec 27.
 */
@PaperParcel
data class Movie(
        val id: Long? = null,
        val title: String? = null,
        val overview: String? = null,
        val runtime: Int? = null,
        val budget: Int? = null,
        val revenue: Int? = null,
        val status: String? = null,
        val tagline: String? = null,
        val genres: List<Genre>? = null,
        @Json(name = "imdb_id") val imdbId: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelMovie.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelMovie.writeToParcel(this, dest, flags)
    }
}