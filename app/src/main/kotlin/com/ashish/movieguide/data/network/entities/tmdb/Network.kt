package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import paperparcel.PaperParcel

/**
 * Created by Ashish on Jan 04.
 */
@PaperParcel
@JsonClass(generateAdapter = true)
data class Network(val id: Long? = null, val name: String? = null) : Parcelable {

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = PaperParcelNetwork.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelNetwork.writeToParcel(this, dest, flags)
    }
}