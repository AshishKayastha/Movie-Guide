package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.data.models.Season
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentView
import com.ashish.movieguide.ui.common.rating.ContentRatingView

/**
 * Created by Ashish on Jan 04.
 */
interface TVShowDetailView : FullDetailContentView<TVShowDetail>, PersonalContentView,
        ContentRatingView {

    fun showSeasonsList(seasonsList: List<Season>)

    fun showSimilarTVShowList(similarTVShowList: List<TVShow>)
}