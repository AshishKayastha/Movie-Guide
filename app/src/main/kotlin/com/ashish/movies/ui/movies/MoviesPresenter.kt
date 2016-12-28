package com.ashish.movies.ui.movies

import com.ashish.movies.data.interactors.MovieInteractor
import com.ashish.movies.ui.base.mvp.RxPresenter
import javax.inject.Inject

/**
 * Created by Ashish on Dec 27.
 */
class MoviesPresenter @Inject constructor(val movieInteractor: MovieInteractor) : RxPresenter<MoviesMvpView>() {

    fun getNowPlayingMoviesList(page: Int?) {
        addSubscription(movieInteractor.getNowPlayingMovies(page)
                .subscribe())
    }
}