package com.ashish.movies.data.models

import android.os.Parcel
import android.os.Parcelable
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import paperparcel.PaperParcel

@PaperParcel
data class Person(
        val id: Long? = null,
        val name: String? = null,
        val popularity: Double? = null,
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