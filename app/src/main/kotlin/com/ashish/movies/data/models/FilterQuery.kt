package com.ashish.movies.data.models

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

/**
 * Created by Ashish on Jan 24.
 */
@PaperParcel
data class FilterQuery(var genreIds: String? = null, var year: Int = 2016) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelFilterQuery.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelFilterQuery.writeToParcel(this, dest, flags)
    }
}