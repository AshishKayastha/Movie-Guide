package com.ashish.movieguide.ui.movie.list

import android.content.Intent
import android.os.Bundle
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import javax.inject.Inject

/**
 * Created by Ashish on Dec 26.
 */
class MovieFragment : RecyclerViewFragment<Movie, RecyclerViewMvpView<Movie>, MoviePresenter>() {

    companion object {
        private const val ARG_MOVIE_TYPE = "movie_type"

        fun newInstance(movieType: Int): MovieFragment {
            val extras = Bundle()
            extras.putInt(ARG_MOVIE_TYPE, movieType)
            val fragment = MovieFragment()
            fragment.arguments = extras
            return fragment
        }

    }

    @Inject lateinit var moviePresenter: MoviePresenter

    override fun providePresenter(): MoviePresenter = moviePresenter

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_MOVIE_TYPE)
    }

    override fun getEmptyTextId(): Int = R.string.no_movies_available

    override fun getEmptyImageId(): Int = R.drawable.ic_movie_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_MOVIE

    override fun getDetailIntent(position: Int): Intent? {
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity!!, movie)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_movie_poster
}