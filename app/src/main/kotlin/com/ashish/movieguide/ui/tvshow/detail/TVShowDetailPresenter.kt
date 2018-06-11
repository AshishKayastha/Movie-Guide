package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.TVShowInteractor
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.CreditResults
import com.ashish.movieguide.data.network.entities.tmdb.ImageItem
import com.ashish.movieguide.data.network.entities.tmdb.TVShowDetail
import com.ashish.movieguide.data.network.entities.tmdb.Videos
import com.ashish.movieguide.data.network.entities.trakt.SyncItems
import com.ashish.movieguide.data.network.entities.trakt.SyncShow
import com.ashish.movieguide.data.network.entities.trakt.TraktShow
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentManager
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_TV
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import java.util.Collections
import javax.inject.Inject

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailPresenter @Inject constructor(
        private val tvShowInteractor: TVShowInteractor,
        private val personalContentManager: PersonalContentManager,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<TVShowDetail, TraktShow, TVShowDetailView>(schedulerProvider) {

    override fun attachView(view: TVShowDetailView) {
        super.attachView(view)
        ratingManager.setView(view)
        personalContentManager.setView(view)
    }

    override fun getDetailContent(id: Long): Observable<FullDetailContent<TVShowDetail, TraktShow>> =
            tvShowInteractor.getFullTVShowDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<TVShowDetail, TraktShow>) {
        super.showDetailContent(fullDetailContent)
        view?.apply {
            setLoadingIndicator(false)
            val tvShowDetail = fullDetailContent.detailContent
            setTMDbRating(tvShowDetail?.voteAverage)
            showItemList(tvShowDetail?.seasons) { showSeasonsList(it) }
            showItemList(tvShowDetail?.similarTVShowResults?.results) { showSimilarTVShowList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<TVShowDetail, TraktShow>): List<String> {
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

    override fun getBackdropImages(detailContent: TVShowDetail): List<ImageItem>? = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: TVShowDetail): List<ImageItem>? = detailContent.images?.posters

    override fun getVideos(detailContent: TVShowDetail): Videos? = detailContent.videos

    override fun getCredits(detailContent: TVShowDetail): CreditResults? = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_tv_detail

    fun markAsFavorite() {
        personalContentManager.markAsFavorite(getTvId(), MEDIA_TYPE_TV)
    }

    fun addToWatchlist() {
        personalContentManager.addToWatchlist(getTvId(), MEDIA_TYPE_TV)
    }

    fun addRating(rating: Int) {
        syncRatings(rating) { syncItems ->
            ratingManager.addRating(syncItems, getTvId(), rating)
        }
    }

    fun removeRating() {
        syncRatings(null) { syncItems ->
            ratingManager.removeRating(syncItems, getTvId())
        }
    }

    private fun syncRatings(rating: Int?, syncRatingAction: (SyncItems) -> Unit) {
        val traktItem = fullDetailContent?.traktItem
        if (traktItem != null) {
            val syncShow = SyncShow(rating, traktItem.ids, "")
            val syncItems = SyncItems(shows = Collections.singletonList(syncShow))
            syncRatingAction.invoke(syncItems)
        }
    }

    private fun getTvId(): Long = fullDetailContent?.detailContent?.id!!

    override fun onDetachView() {
        super.onDetachView()
        ratingManager.setView(null)
        personalContentManager.setView(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        ratingManager.unsubscribe()
        personalContentManager.unsubscribe()
    }
}