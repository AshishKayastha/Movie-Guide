package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.models.tmdb.FullDetailContent
import com.ashish.movieguide.data.models.tmdb.TVShowDetail
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentManager
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_TV
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.getRatingValue
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
@ActivityScope
class TVShowDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val personalContentManager: PersonalContentManager,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<TVShowDetail, TVShowDetailView>(schedulerProvider) {

    override fun attachView(view: TVShowDetailView) {
        super.attachView(view)
        ratingManager.setView(view)
        personalContentManager.setView(view)
    }

    override fun getDetailContent(id: Long) = tvShowInteractor.getFullTVShowDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<TVShowDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val tvShowDetail = fullDetailContent.detailContent
            val accountState = tvShowDetail?.tvRatings
            personalContentManager.setAccountState(accountState)

            showSavedRating(accountState?.getRatingValue())
            setTMDbRating(tvShowDetail?.voteAverage)
            showItemList(tvShowDetail?.seasons) { showSeasonsList(it) }
            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<TVShowDetail>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.apply {
                add(overview ?: "")
                add(genres.convertListToCommaSeparatedText { it.name.toString() })
                add(omdbDetail?.Rated ?: "")
                add(status ?: "")
                add(omdbDetail?.Awards ?: "")
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
                add(firstAirDate.getFormattedMediumDate())
                add(lastAirDate.getFormattedMediumDate())
                add(numberOfSeasons.toString())
                add(numberOfEpisodes.toString())
                add(networks.convertListToCommaSeparatedText { it.name.toString() })
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: TVShowDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: TVShowDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: TVShowDetail) = detailContent.videos

    override fun getCredits(detailContent: TVShowDetail) = detailContent.creditsResults

    override fun getErrorMessageId() = R.string.error_load_tv_detail

    fun markAsFavorite() {
        personalContentManager.markAsFavorite(getTvId(), MEDIA_TYPE_TV)
    }

    fun addToWatchlist() {
        personalContentManager.addToWatchlist(getTvId(), MEDIA_TYPE_TV)
    }

    fun saveRating(rating: Double) {
        val tvId = getTvId()
        ratingManager.saveRating(tvShowInteractor.rateTVShow(tvId, rating), tvId, rating)
    }

    fun deleteRating() {
        val tvId = getTvId()
        ratingManager.deleteRating(tvShowInteractor.deleteTVRating(tvId), tvId)
    }

    private fun getTvId() = fullDetailContent?.detailContent?.id!!

    override fun detachView() {
        super.detachView()
        ratingManager.setView(null)
        personalContentManager.setView(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        ratingManager.unsubscribe()
        personalContentManager.unsubscribe()
    }
}