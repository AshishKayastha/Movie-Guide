package com.ashish.movieguide.data.network.entities.tmdb

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

@PaperParcel
data class Person(
        val id: Long? = null,
        val name: String? = null,
        @Json(name = "profile_path") val profilePath: String? = null
) : ViewType, Parcelable {

    override fun getViewType() = CONTENT_VIEW

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = PaperParcelPerson.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelPerson.writeToParcel(this, dest, flags)
    }
}