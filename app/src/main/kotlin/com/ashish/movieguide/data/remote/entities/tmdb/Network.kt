package com.ashish.movieguide.data.remote.entities.tmdb

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashish on Jan 04.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class Network(val id: Long? = null, val name: String? = null) : Parcelable