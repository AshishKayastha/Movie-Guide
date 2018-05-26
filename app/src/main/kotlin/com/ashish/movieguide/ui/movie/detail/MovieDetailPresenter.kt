package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.network.entities.common.FullDetailContent
import com.ashish.movieguide.data.network.entities.tmdb.MovieDetail
import com.ashish.movieguide.data.network.entities.trakt.SyncItems
import com.ashish.movieguide.data.network.entities.trakt.SyncMovie
import com.ashish.movieguide.data.network.entities.trakt.TraktMovie
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentManager
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedCurrency
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.getFormattedRuntime
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import java.util.ArrayList
import java.util.Collections
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
class MovieDetailPresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        private val personalContentManager: PersonalContentManager,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<MovieDetail, TraktMovie, MovieDetailView>(schedulerProvider) {

    override fun attachView(view: MovieDetailView) {
        super.attachView(view)
        ratingManager.setView(view)
        personalContentManager.setView(view)
    }

    override fun getDetailContent(id: Long): Observable<FullDetailContent<MovieDetail, TraktMovie>> =
            movieInteractor.getMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail, TraktMovie>) {
        super.showDetailContent(fullDetailContent)
        view?.run {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
            setTMDbRating(movieDetail?.voteAverage)
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<MovieDetail, TraktMovie>): List<String> {
        val contentList = ArrayList<String>()
        fullDetailContent.detailContent?.apply {
            val omdbDetail = fullDetailContent.omdbDetail
            contentList.apply {
                add(overview ?: "")
                add(tagline ?: "")
                add(genres.convertListToCommaSeparatedText { it.name.toString() })
                add(omdbDetail?.Rated ?: "")
                add(status ?: "")
                add(omdbDetail?.Awards ?: "")
                add(omdbDetail?.Production ?: "")
                add(omdbDetail?.Country ?: "")
                add(omdbDetail?.Language ?: "")
                add(releaseDate.getFormattedMediumDate())
                add(runtime.getFormattedRuntime())
                add(budget.getFormattedCurrency())
                add(revenue.getFormattedCurrency())
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: MovieDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: MovieDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: MovieDetail) = detailContent.videos

    override fun getCredits(detailContent: MovieDetail) = detailContent.credits

    override fun getErrorMessageId() = R.string.error_load_movie_detail

    fun markAsFavorite() {
        personalContentManager.markAsFavorite(getMovieId(), MEDIA_TYPE_MOVIE)
    }

    fun addToWatchlist() {
        personalContentManager.addToWatchlist(getMovieId(), MEDIA_TYPE_MOVIE)
    }

    fun addRating(rating: Int) {
        syncRatings(rating) { syncItems ->
            ratingManager.addRating(syncItems, getMovieId(), rating)
        }
    }

    fun removeRating() {
        syncRatings(null) { syncItems ->
            ratingManager.removeRating(syncItems, getMovieId())
        }
    }

    private fun syncRatings(rating: Int?, syncRatingAction: (SyncItems) -> Unit) {
        val traktItem = fullDetailContent?.traktItem
        if (traktItem != null) {
            val syncMovie = SyncMovie(rating, traktItem.ids, "")
            val syncItems = SyncItems(Collections.singletonList(syncMovie))
            syncRatingAction.invoke(syncItems)
        }
    }

    private fun getMovieId() = fullDetailContent?.detailContent?.id!!

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