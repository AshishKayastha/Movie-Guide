package com.ashish.movieguide.data.network.entities.tmdb

import com.ashish.movieguide.ui.base.adapter.RecyclerViewItem
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
        val id: String? = null,
        val author: String? = null,
        val content: String? = null,
        val url: String? = null
) : RecyclerViewItem {

    override fun getViewType() = RecyclerViewItem.CONTENT_VIEW
}