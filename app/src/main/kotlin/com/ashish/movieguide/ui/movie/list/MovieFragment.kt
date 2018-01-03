package com.ashish.movieguide.ui.movie.list

import android.content.Intent
import android.os.Bundle
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE

/**
 * Created by Ashish on Dec 26.
 */
class MovieFragment : BaseRecyclerViewFragment<Movie,
        BaseRecyclerViewMvpView<Movie>, MoviePresenter>() {

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

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(MovieFragment::class.java, MovieComponent.Builder::class.java)
                .withModule(FragmentModule(activity!!))
                .build()
                .inject(this)
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_MOVIE_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getEmptyTextId() = R.string.no_movies_available

    override fun getEmptyImageId() = R.drawable.ic_movie_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return if (activity != null) {
            MovieDetailActivity.createIntent(activity!!, movie)
        } else {
            return null
        }
    }
}