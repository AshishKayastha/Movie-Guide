package com.ashish.movies.ui.movie.detail

import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MovieDetail
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailView : FullDetailContentView<MovieDetail> {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}