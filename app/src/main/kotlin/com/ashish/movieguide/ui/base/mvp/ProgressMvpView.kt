package com.ashish.movieguide.ui.base.mvp

import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged

/**
 * Base MVP interface to handle showing or hiding progress bar
 * depending upon whether data is loading or loaded.
 */
interface ProgressMvpView : MvpView {

    @DistinctUntilChanged
    fun setLoadingIndicator(showIndicator: Boolean)
}