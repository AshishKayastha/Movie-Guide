package com.ashish.movieguide.ui.base.detail.fulldetail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.ui.base.detail.BaseDetailPresenter
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider

/**
 * Created by Ashish on Jan 20.
 */
abstract class FullDetailContentPresenter<I, V : FullDetailContentView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : BaseDetailPresenter<I, V>(schedulerProvider) {

    override fun showDetailContent(fullDetailContent: FullDetailContent<I>) {
        fullDetailContent.omdbDetail?.apply {
            if (isValidRating(imdbRating) || isValidRating(tomatoRating) || isValidRating(tomatoUserRating)
                    || isValidRating(Metascore)) {
                getView()?.showRatingCard()
                setMetaScore(Metascore)
                setIMDbRating(imdbRating)
                setTomatoRating(tomatoMeter, tomatoImage)
                setAudienceScore(tomatoUserRating, tomatoUserMeter)
            }
        }

        super.showDetailContent(fullDetailContent)
    }

    protected fun setTMDbRating(voteAverage: Double?) {
        val tmdbRating = voteAverage.toString()
        if (isValidRating(tmdbRating)) {
            getView()?.apply {
                showRatingCard()
                showTmdbRating(tmdbRating)
            }
        }
    }

    private fun setIMDbRating(imdbRating: String?) {
        if (isValidRating(imdbRating)) getView()?.showImdbRating(imdbRating!!)
    }

    private fun setTomatoRating(tomatoRating: String?, tomatoImage: String?) {
        if (isValidRating(tomatoRating)) {
            getView()?.apply {
                if (tomatoImage == "certified") {
                    showRottenTomatoesRating(tomatoRating!!, R.drawable.ic_rt_certified)
                } else if (tomatoImage == "fresh") {
                    showRottenTomatoesRating(tomatoRating!!, R.drawable.ic_rt_fresh)
                } else if (tomatoImage == "rotten") {
                    showRottenTomatoesRating(tomatoRating!!, R.drawable.ic_rt_rotten)
                }
            }
        }
    }

    private fun setAudienceScore(tomatoUserRating: String?, tomatoUserMeter: String?) {
        if (isValidRating(tomatoUserRating)) {
            getView()?.apply {
                val flixterScore = tomatoUserRating!!.toFloat()
                if (flixterScore >= 3.5) {
                    showAudienceScore(tomatoUserMeter!!, R.drawable.ic_audience_score_good)
                } else {
                    showAudienceScore(tomatoUserMeter!!, R.drawable.ic_audience_score_bad)
                }
            }
        }
    }

    private fun setMetaScore(metaScore: String?) {
        if (isValidRating(metaScore)) {
            getView()?.apply {
                val metaScoreInt = metaScore!!.toInt()
                if (metaScoreInt > 60) {
                    showMetaScore(metaScore, 0xFF66CC33.toInt())
                } else if (metaScoreInt in 41..60) {
                    showMetaScore(metaScore, 0xFFFFCC33.toInt())
                } else {
                    showMetaScore(metaScore, 0xFFFF0000.toInt())
                }
            }
        }
    }

    private fun isValidRating(rating: String?): Boolean {
        return rating.isNotNullOrEmpty() && rating != "N/A" && rating != "0"
    }
}