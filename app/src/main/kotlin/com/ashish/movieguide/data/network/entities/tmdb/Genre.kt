package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import paperparcel.PaperParcel

@PaperParcel
@JsonClass(generateAdapter = true)
data class Genre(val id: Long? = null, val name: String? = null) : Parcelable {

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = PaperParcelGenre.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelGenre.writeToParcel(this, dest, flags)
    }
}