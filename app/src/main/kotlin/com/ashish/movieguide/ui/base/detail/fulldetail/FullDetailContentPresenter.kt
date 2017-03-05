package com.ashish.movieguide.ui.base.detail.fulldetail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.VideoItem
import com.ashish.movieguide.data.models.Videos
import com.ashish.movieguide.data.models.YouTubeVideo
import com.ashish.movieguide.ui.base.detail.BaseDetailPresenter
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider

/**
 * Created by Ashish on Jan 20.
 */
abstract class FullDetailContentPresenter<I, V : FullDetailContentView<I>>(
        schedulerProvider: BaseSchedulerProvider
) : BaseDetailPresenter<I, V>(schedulerProvider) {

    companion object {
        private const val YOUTUBE_SITE = "YouTube"
        private const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
        private const val YOUTUBE_THUMB_URL = "https://img.youtube.com/vi/{id}/hqdefault.jpg"
    }

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

        fullDetailContent.detailContent?.let { handleVideoContents(it) }
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

    private fun handleVideoContents(detailContent: I) {
        val videoResults = getVideos(detailContent)?.results
        if (videoResults.isNotNullOrEmpty()) {
            showYouTubeTrailer(videoResults)
            showYouTubeVideos(videoResults)
        }
    }

    private fun showYouTubeTrailer(videoResults: List<VideoItem>?) {
        val youtubeTrailerUrl = videoResults!!.firstOrNull { it.site == YOUTUBE_SITE }?.key
        if (youtubeTrailerUrl.isNotNullOrEmpty()) {
            getView()?.showTrailerFAB(YOUTUBE_BASE_URL + youtubeTrailerUrl!!)
        }
    }

    private fun showYouTubeVideos(videoResults: List<VideoItem>?) {
        val youTubeVideos = ArrayList<YouTubeVideo>()

        videoResults!!.filter { (_, _, site, _, key) ->
            site == YOUTUBE_SITE && key.isNotNullOrEmpty()
        }.forEach { (_, name, _, _, key) ->
            val imageUrl = YOUTUBE_THUMB_URL.replace("{id}", key!!)
            youTubeVideos.add(YouTubeVideo(name, YOUTUBE_BASE_URL + key, imageUrl))
        }

        getView()?.showYouTubeVideos(youTubeVideos)
    }

    abstract fun getVideos(detailContent: I): Videos?
}