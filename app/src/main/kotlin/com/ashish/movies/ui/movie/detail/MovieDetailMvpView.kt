package com.ashish.movies.ui.movie.detail

import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailMvpView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailMvpView : BaseDetailMvpView<MovieDetail> {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}