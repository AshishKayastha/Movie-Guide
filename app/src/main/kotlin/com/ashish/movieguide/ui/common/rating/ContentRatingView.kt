package com.ashish.movieguide.ui.common.rating

import com.ashish.movieguide.ui.base.mvp.MvpView

interface ContentRatingView : MvpView {

    fun showSavedRating(rating: Double?)
}