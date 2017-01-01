package com.ashish.movies.ui.movie

import com.ashish.movies.data.api.MovieService.Companion.NOW_PLAYING
import com.ashish.movies.data.api.MovieService.Companion.POPULAR
import com.ashish.movies.data.api.MovieService.Companion.TOP_RATED
import com.ashish.movies.data.api.MovieService.Companion.UPCOMING
import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviePresenter @Inject constructor(val movieInteractor: MovieInteractor)
    : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>() {

    companion object {
        private val MOVIE_TYPES = arrayOf(NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING)
    }

    override fun loadData(type: Int?, page: Int, showProgress: Boolean) {
        getDataByType(MOVIE_TYPES[type ?: 0], page, showProgress)
    }

    override fun loadMoreData(type: Int?, page: Int) {
        if (page <= totalPages) getMoreDataByType(MOVIE_TYPES[type ?: 0], page)
    }

    override fun getData(type: String?, page: Int) = movieInteractor.getMoviesByType(type, page)
}