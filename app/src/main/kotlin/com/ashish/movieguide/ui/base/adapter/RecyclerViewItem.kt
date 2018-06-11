package com.ashish.movieguide.ui.base.adapter

interface RecyclerViewItem {

    companion object {
        const val LOADING_VIEW = 0
        const val CONTENT_VIEW = 1
        const val ERROR_VIEW = 2
    }

    fun getViewType(): Int
}