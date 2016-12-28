package com.ashish.movies.ui.movies

import com.ashish.movies.data.models.Movie
import com.ashish.movies.ui.base.mvp.LceView

/**
 * Created by Ashish on Dec 27.
 */
interface MoviesMvpView : LceView {

    fun showMoviesList(moviesList: List<Movie>?)
}