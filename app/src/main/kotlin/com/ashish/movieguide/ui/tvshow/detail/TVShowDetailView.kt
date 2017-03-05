package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.data.models.Season
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView

/**
 * Created by Ashish on Jan 04.
 */
interface TVShowDetailView : FullDetailContentView<TVShowDetail> {

    fun showSeasonsList(seasonsList: List<Season>)

    fun showSimilarTVShowList(similarTVShowList: List<TVShow>)

    fun setFavoriteIcon(isFavorite: Boolean)

    fun animateFavoriteIcon(isFavorite: Boolean)
}