package com.ashish.movieguide.data.remote.entities.tmdb

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Genre(val id: Long? = null, val name: String? = null) : Parcelable