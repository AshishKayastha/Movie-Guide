package com.ashish.movieguide.ui.base.mvp

import com.ashish.movieguide.ui.widget.CustomToast
import net.grandcentrix.thirtyinch.TiView

/**
 * Base interface from which every other MVP view should extend from.
 */
interface MvpView : TiView {

    fun showMessage(messageId: Int)

    fun showToastMessage(messageId: Int, msgType: Int = CustomToast.TOAST_TYPE_ERROR)
}