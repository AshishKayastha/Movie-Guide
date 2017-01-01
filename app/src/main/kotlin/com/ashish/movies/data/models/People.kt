package com.ashish.movies.data.models

import com.ashish.movies.ui.common.adapter.ViewType
import com.squareup.moshi.Json

data class People(
        val id: Long? = null,
        val name: String? = null,
        val adult: Boolean? = null,
        val popularity: Double? = null,
        @Json(name = "profile_path") val profilePath: String? = null
) : ViewType {

    override fun getViewType() = ViewType.CONTENT_VIEW
}