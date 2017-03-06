package com.ashish.movieguide.ui.common.personalcontent

import com.ashish.movieguide.ui.base.mvp.MvpView

interface PersonalContentView : MvpView {

    fun setFavoriteIcon(isFavorite: Boolean)

    fun animateFavoriteIcon(isFavorite: Boolean)

    fun changeWatchlistMenuItem(isInWatchlist: Boolean)
}