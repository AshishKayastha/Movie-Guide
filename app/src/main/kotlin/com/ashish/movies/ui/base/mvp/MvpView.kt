package com.ashish.movies.ui.base.mvp

/**
 * Base interface from which every other MVP view should extend from.
 */
interface MvpView {

    fun showToastMessage(messageId: Int)

    fun showMessage(messageId: Int)
}