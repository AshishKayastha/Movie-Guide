package com.ashish.movies.ui.movie

import android.os.Bundle
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_MOVIE
import com.ashish.movies.ui.moviedetail.MovieDetailActivity

/**
 * Created by Ashish on Dec 26.
 */
class MovieFragment : BaseRecyclerViewFragment<Movie, BaseRecyclerViewMvpView<Movie>, MoviePresenter>() {

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

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MovieModule()).inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView.setText(R.string.no_movies_available)
        emptyImageView.setImageResource(R.drawable.ic_movie_white_100dp)
    }

    override fun getFragmentArguments() {
        type = arguments.getInt(ARG_MOVIE_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun onItemClick(position: Int) {
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        startActivity(MovieDetailActivity.createIntent(activity, movie))
    }
}