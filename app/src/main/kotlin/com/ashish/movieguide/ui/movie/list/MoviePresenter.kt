package com.ashish.movieguide.ui.movie.list

import com.ashish.movieguide.data.remote.api.tmdb.MovieApi.Companion.NOW_PLAYING
import com.ashish.movieguide.data.remote.api.tmdb.MovieApi.Companion.POPULAR
import com.ashish.movieguide.data.remote.api.tmdb.MovieApi.Companion.TOP_RATED
import com.ashish.movieguide.data.remote.api.tmdb.MovieApi.Companion.UPCOMING
import com.ashish.movieguide.data.remote.entities.tmdb.Movie
import com.ashish.movieguide.data.remote.entities.tmdb.Results
import com.ashish.movieguide.data.remote.interactors.MovieInteractor
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewPresenter
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviePresenter @Inject constructor(
        private val movieInteractor: MovieInteractor,
        schedulerProvider: BaseSchedulerProvider
) : RecyclerViewPresenter<Movie, RecyclerViewMvpView<Movie>>(schedulerProvider) {

    companion object {
        private val MOVIE_TYPES = arrayOf(NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING)
    }

    override fun getType(type: Int?): String? = MOVIE_TYPES[type ?: 0]

    override fun getResults(type: String?, page: Int): Single<Results<Movie>> =
            movieInteractor.getMoviesByType(type, page)
}