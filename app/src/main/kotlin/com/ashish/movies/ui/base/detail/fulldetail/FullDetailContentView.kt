package com.ashish.movies.ui.base.detail.fulldetail

import com.ashish.movies.ui.base.detail.BaseDetailView

/**
 * Created by Ashish on Jan 20.
 */
interface FullDetailContentView<in I> : BaseDetailView<I> {

    fun showRatingCard()

    fun showImdbRating(imdbRating: String)

    fun showRottenTomatoesRating(tomatoMeter: String, drawableResId: Int)

    fun showAudienceScore(audienceScore: String, drawableResId: Int)

    fun showMetaScore(metaScore: String, color: Int)

    fun showTmdbRating(tmdbRating: String)
}