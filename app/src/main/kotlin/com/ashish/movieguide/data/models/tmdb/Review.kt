package com.ashish.movieguide.data.models.tmdb

import com.ashish.movieguide.ui.common.adapter.ViewType

data class Review(
        val id: String? = null,
        val author: String? = null,
        val content: String? = null,
        val url: String? = null
) : ViewType {

    override fun getViewType() = ViewType.CONTENT_VIEW
}