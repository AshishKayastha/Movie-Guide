package com.ashish.movieguide.ui.base.mvp

import net.grandcentrix.thirtyinch.TiView

/**
 * Base interface from which every other MVP view should extend from.
 */
interface MvpView : TiView {

    fun showToastMessage(messageId: Int)

    fun showMessage(messageId: Int)
}