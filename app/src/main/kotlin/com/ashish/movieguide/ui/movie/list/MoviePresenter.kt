package com.ashish.movieguide.ui.movie.list

import com.ashish.movieguide.data.api.tmdb.MovieApi.Companion.NOW_PLAYING
import com.ashish.movieguide.data.api.tmdb.MovieApi.Companion.POPULAR
import com.ashish.movieguide.data.api.tmdb.MovieApi.Companion.TOP_RATED
import com.ashish.movieguide.data.api.tmdb.MovieApi.Companion.UPCOMING
import com.ashish.movieguide.data.interactors.MovieInteractor
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviePresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        schedulerProvider: BaseSchedulerProvider
) : BaseRecyclerViewPresenter<Movie, BaseRecyclerViewMvpView<Movie>>(schedulerProvider) {

    companion object {
        @JvmStatic
        private val MOVIE_TYPES = arrayOf(NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING)
    }

    override fun getType(type: Int?) = MOVIE_TYPES[type ?: 0]

    override fun getResultsObservable(type: String?, page: Int) = movieInteractor.getMoviesByType(type, page)
}