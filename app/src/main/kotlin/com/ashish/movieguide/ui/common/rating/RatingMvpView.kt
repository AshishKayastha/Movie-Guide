package com.ashish.movieguide.ui.common.rating

import com.ashish.movieguide.ui.base.mvp.MvpView

interface RatingMvpView : MvpView {

    fun showSavedRating(rating: Double?)
}