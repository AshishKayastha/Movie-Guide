package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.data.network.entities.tmdb.Season
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.data.network.entities.tmdb.TVShowDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentView
import com.ashish.movieguide.ui.common.rating.RatingMvpView

/**
 * Created by Ashish on Jan 04.
 */
interface TVShowDetailView : FullDetailContentView<TVShowDetail>, PersonalContentView,
        RatingMvpView {

    fun showSeasonsList(seasonsList: List<Season>)

    fun showSimilarTVShowList(similarTVShowList: List<TVShow>)
}