package com.ashish.movies.ui.movie

import com.ashish.movies.data.api.MovieService.Companion.NOW_PLAYING
import com.ashish.movies.data.api.MovieService.Companion.POPULAR
import com.ashish.movies.data.api.MovieService.Companion.TOP_RATED
import com.ashish.movies.data.api.MovieService.Companion.UPCOMING
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.MovieResults
import com.ashish.movies.ui.base.mvp.RxPresenter
import com.ashish.movies.ui.movie.MovieFragment.Companion.POPULAR_MOVIES
import com.ashish.movies.ui.movie.MovieFragment.Companion.TOP_RATED_MOVIES
import com.ashish.movies.ui.movie.MovieFragment.Companion.UPCOMING_MOVIES
import com.ashish.movies.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviePresenter @Inject constructor(val movieInteractor: MovieInteractor) : RxPresenter<MovieMvpView>() {

    private var totalPages = 1

    fun getMovieList(movieType: Int?, page: Int = 1, showProgress: Boolean = true) {
        if (Utils.isOnline()) {
            when (movieType) {
                POPULAR_MOVIES -> getMoviesByType(POPULAR, page, showProgress)
                TOP_RATED_MOVIES -> getMoviesByType(TOP_RATED, page, showProgress)
                UPCOMING_MOVIES -> getMoviesByType(UPCOMING, page, showProgress)
                else -> getMoviesByType(NOW_PLAYING, page, showProgress)
            }
        }
    }

    private fun getMoviesByType(movieType: String, page: Int, showProgress: Boolean) {
        if (showProgress) getView()?.showProgress()
        addSubscription(movieInteractor.getMoviesByType(movieType, page)
                .doOnNext { movieResults -> totalPages = movieResults.totalPages }
                .subscribe({ movieResults -> showMovieList(movieResults) }, { t -> handleGetMovieError(t) }))
    }

    private fun showMovieList(movieResults: MovieResults?) {
        getView()?.apply {
            showItemList(movieResults?.results)
            hideProgress()
        }
    }

    fun loadMoreMovies(movieType: Int?, page: Int) {
        if (Utils.isOnline()) {
            if (page <= totalPages) {
                when (movieType) {
                    POPULAR_MOVIES -> getMoreMoviesByType(POPULAR, page)
                    TOP_RATED_MOVIES -> getMoreMoviesByType(TOP_RATED, page)
                    UPCOMING_MOVIES -> getMoreMoviesByType(UPCOMING, page)
                    else -> getMoreMoviesByType(NOW_PLAYING, page)
                }
            }
        }
    }

    private fun getMoreMoviesByType(movieType: String, page: Int) {
        addSubscription(movieInteractor.getMoviesByType(movieType, page)
                .subscribe({ movieResults -> addMovieItems(movieResults) }, { t -> handleGetMovieError(t) }))
    }

    private fun addMovieItems(movieResults: MovieResults?) {
        getView()?.apply {
            addNewItems(movieResults?.results)
            hideProgress()
        }
    }

    private fun handleGetMovieError(t: Throwable) {
        Timber.e(t)
        getView()?.apply {
            hideProgress()
        }
    }
}