package com.ashish.movies.ui.movie

import android.os.Bundle
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment

/**
 * Created by Ashish on Dec 26.
 */
class MovieFragment : BaseRecyclerViewFragment<Movie, MovieMvpView, MoviePresenter>(), MovieMvpView {

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

    override fun initView() {
        type = arguments.getInt(ARG_MOVIE_TYPE)
        recyclerViewAdapter = MovieAdapter()
    }
}