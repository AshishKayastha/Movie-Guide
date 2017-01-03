package com.ashish.movies.data.models

import com.ashish.movies.ui.common.adapter.ViewType
import com.squareup.moshi.Json

/**
 * Created by Ashish on Jan 03.
 */
data class Credit(
        val id: Long? = null,
        val name: String? = null,
        val job: String? = null,
        val character: String? = null,
        val department: String? = null,
        @Json(name = "credit_id") val creditId: String? = null,
        @Json(name = "profile_path") val profilePath: String? = null
) : ViewType {

    override fun getViewType() = ViewType.CONTENT_VIEW
}