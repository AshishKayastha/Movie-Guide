package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

@PaperParcel
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