package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashish on Jan 24.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class FilterQuery(
        var sortBy: String = "popularity.desc",
        var genreIds: String? = null,
        var minDate: String? = null,
        var maxDate: String? = null
) : Parcelable