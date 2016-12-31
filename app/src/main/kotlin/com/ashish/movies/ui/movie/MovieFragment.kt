package com.ashish.movies.ui.movie

import android.os.Bundle
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.common.RecyclerViewScrollListener

/**
 * Created by Ashish on Dec 26.
 */
class MovieFragment : BaseRecyclerViewFragment<Movie, MovieMvpView, MoviePresenter>(), MovieMvpView {

    private var movieType: Int? = null

    companion object {
        const val ARG_MOVIE_TYPE = "movie_type"

        const val POPULAR_MOVIES = 1
        const val TOP_RATED_MOVIES = 2
        const val UPCOMING_MOVIES = 3

        fun newInstance(movieType: Int): MovieFragment {
            val extras = Bundle()
            extras.putInt(ARG_MOVIE_TYPE, movieType)
            val fragment = MovieFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    private val scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
        override fun onLoadMore(currentPage: Int) {
            if (currentPage > 1) presenter.loadMoreMovies(movieType, currentPage)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(MovieModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_recycler_view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addOnScrollListener(scrollListener)
        presenter.getMovieList(movieType)
    }

    override fun initView() {
        movieType = arguments.getInt(ARG_MOVIE_TYPE)
        recyclerViewAdapter = MovieAdapter()
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.getMovieList(movieType, showProgress = recyclerViewAdapter.itemCount == 0)
    }
}