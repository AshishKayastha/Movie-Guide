package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.data.models.tmdb.MovieDetail
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentView
import com.ashish.movieguide.ui.common.rating.RatingMvpView

/**
 * Created by Ashish on Jan 03.
 */
interface MovieDetailView : FullDetailContentView<MovieDetail>, PersonalContentView,
        RatingMvpView {

    fun showSimilarMoviesList(similarMoviesList: List<Movie>)
}