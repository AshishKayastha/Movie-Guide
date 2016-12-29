package com.ashish.movies.ui.movies

import com.ashish.movies.data.api.MovieService.Companion.NOW_PLAYING
import com.ashish.movies.data.api.MovieService.Companion.POPULAR
import com.ashish.movies.data.api.MovieService.Companion.TOP_RATED
import com.ashish.movies.data.api.MovieService.Companion.UPCOMING
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.MovieResults
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.movies.MoviesFragment.Companion.POPULAR_MOVIES
import com.ashish.movies.ui.movies.MoviesFragment.Companion.TOP_RATED_MOVIES
import com.ashish.movies.ui.movies.MoviesFragment.Companion.UPCOMING_MOVIES
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviesPresenter @Inject constructor(val movieInteractor: MovieInteractor) : RxPresenter<MoviesMvpView>() {

    fun getMovieList(movieType: Int?, page: Int? = null, showProgress: Boolean = true) {
        when (movieType) {
            POPULAR_MOVIES -> getMoviesByType(POPULAR, page, showProgress)
            TOP_RATED_MOVIES -> getMoviesByType(TOP_RATED, page, showProgress)
            UPCOMING_MOVIES -> getMoviesByType(UPCOMING, page, showProgress)
            else -> getMoviesByType(NOW_PLAYING, page, showProgress)
        }
    }

    private fun getMoviesByType(movieType: String, page: Int?, showProgress: Boolean) {
        if (showProgress) getView()?.showProgress()
        addSubscription(movieInteractor.getMoviesByType(movieType, page)
                .subscribe({ movieResults -> showMovieList(movieResults) }, { t -> handleGetMovieError(t) }))
    }

    private fun showMovieList(movieResults: MovieResults?) {
        getView()?.hideProgress()
        getView()?.showMoviesList(movieResults?.results)
    }

    private fun handleGetMovieError(t: Throwable) {
        Timber.e(t)
        getView()?.hideProgress()
    }
}