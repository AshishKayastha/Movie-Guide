package com.ashish.movieguide.data.models.tmdb

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

/**
 * Created by Ashish on Jan 24.
 */
@PaperParcel
data class FilterQuery(
        var sortBy: String = "popularity.desc",
        var genreIds: String? = null,
        var minDate: String? = null,
        var maxDate: String? = null
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelFilterQuery.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelFilterQuery.writeToParcel(this, dest, flags)
    }
}