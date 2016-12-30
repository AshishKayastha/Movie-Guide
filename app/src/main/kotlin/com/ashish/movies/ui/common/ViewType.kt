package com.ashish.movies.ui.common

/**
 * Created by Ashish on Dec 30.
 */
interface ViewType {

    companion object {
        const val LOADING_VIEW = 0
        const val CONTENT_VIEW = 1
    }

    fun getViewType(): Int
}