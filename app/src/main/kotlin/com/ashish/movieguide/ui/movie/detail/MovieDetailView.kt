package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.MovieDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailView : FullDetailContentView<MovieDetail> {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}