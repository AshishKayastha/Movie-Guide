package com.ashish.movies.ui.movie.detail

import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.BaseDetailView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailView : BaseDetailView<MovieDetail> {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}