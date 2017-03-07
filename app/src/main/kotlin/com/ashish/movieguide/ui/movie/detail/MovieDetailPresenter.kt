package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.R
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.models.FullDetailContent
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.di.scopes.ActivityScope
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentPresenter
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentManager
import com.ashish.movieguide.ui.common.rating.RatingManager
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.extensions.convertListToCommaSeparatedText
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.getFormattedNumber
import com.ashish.movieguide.utils.extensions.getFormattedRuntime
import com.ashish.movieguide.utils.extensions.getRatingValue
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Ashish on Dec 31.
 */
@ActivityScope
class MovieDetailPresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        private val personalContentManager: PersonalContentManager,
        private val ratingManager: RatingManager,
        schedulerProvider: BaseSchedulerProvider
) : FullDetailContentPresenter<MovieDetail, MovieDetailView>(schedulerProvider) {

    override fun attachView(view: MovieDetailView) {
        super.attachView(view)
        ratingManager.setView(view)
        personalContentManager.setView(view)
    }

    override fun getDetailContent(id: Long) = movieInteractor.getFullMovieDetail(id)

    override fun showDetailContent(fullDetailContent: FullDetailContent<MovieDetail>) {
        super.showDetailContent(fullDetailContent)
        getView()?.apply {
            hideProgress()
            val movieDetail = fullDetailContent.detailContent
            val accountState = movieDetail?.movieRatings
            personalContentManager.setAccountState(accountState)

            showSavedRating(accountState?.getRatingValue())
            setTMDbRating(movieDetail?.voteAverage)
            showItemList(movieDetail?.similarMovieResults?.results) { showSimilarMoviesList(it) }
        }
    }

    override fun getContentList(fullDetailContent: FullDetailContent<MovieDetail>): List<String> {
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
                add(budget.getFormattedNumber())
                add(revenue.getFormattedNumber())
            }
        }

        return contentList
    }

    override fun getBackdropImages(detailContent: MovieDetail) = detailContent.images?.backdrops

    override fun getPosterImages(detailContent: MovieDetail) = detailContent.images?.posters

    override fun getVideos(detailContent: MovieDetail) = detailContent.videos

    override fun getCredits(detailContent: MovieDetail) = detailContent.creditsResults

    override fun getErrorMessageId() = R.string.error_load_movie_detail

    fun markAsFavorite() {
        personalContentManager.markAsFavorite(getMovieId(), MEDIA_TYPE_MOVIE)
    }

    fun addToWatchlist() {
        personalContentManager.addToWatchlist(getMovieId(), MEDIA_TYPE_MOVIE)
    }

    fun saveRating(rating: Double) {
        val movieId = getMovieId()
        ratingManager.saveRating(movieInteractor.rateMovie(movieId, rating), movieId, rating)
    }

    fun deleteRating() {
        val movieId = getMovieId()
        ratingManager.deleteRating(movieInteractor.deleteMovieRating(movieId), movieId)
    }

    private fun getMovieId() = fullDetailContent?.detailContent?.id!!

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