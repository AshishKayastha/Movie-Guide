package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.data.models.Season
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.ui.base.detail.BaseDetailView

/**
 * Created by Ashish on Jan 04.
 */
interface TVShowDetailView : BaseDetailView<TVShowDetail> {

    fun showSeasonsList(seasonsList: List<Season>)

    fun showSimilarTVShowList(similarTVShowList: List<TVShow>)
}