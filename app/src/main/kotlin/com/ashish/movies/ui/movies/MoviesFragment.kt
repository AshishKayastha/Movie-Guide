package com.ashish.movies.ui.movies

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.extensions.hide
import com.ashish.movies.extensions.setVisibility
import com.ashish.movies.extensions.show
import com.ashish.movies.listeners.RecyclerViewScrollListener
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_material_progress_bar.*

/**
 * Created by Ashish on Dec 26.
 */
class MoviesFragment : BaseFragment<MoviesMvpView, MoviesPresenter>(), MoviesMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    private var movieType: Int? = null
    private var moviesAdapter: MoviesAdapter? = null

    companion object {

        const val ARG_MOVIE_TYPE = "movie_type"

        const val POPULAR_MOVIES = 1
        const val TOP_RATED_MOVIES = 2
        const val UPCOMING_MOVIES = 3

        fun newInstance(movieType: Int): MoviesFragment {
            val extras = Bundle()
            extras.putInt(ARG_MOVIE_TYPE, movieType)
            val fragment = MoviesFragment()
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
        appComponent.plus(MoviesModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_movies

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieType = arguments.getInt(ARG_MOVIE_TYPE)

        recyclerView.setHasFixedSize(true)
        recyclerView.emptyView = emptyView
        recyclerView.addItemDecoration(ItemOffsetDecoration())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.addOnScrollListener(scrollListener)

        moviesAdapter = MoviesAdapter()
        recyclerView.adapter = moviesAdapter

        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setSwipeableViews(emptyView, recyclerView)
        swipeRefresh.setOnRefreshListener(this)

        presenter.getMovieList(movieType)
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.getMovieList(movieType, showProgress = moviesAdapter?.itemCount == 0)
    }

    override fun showProgress() {
        emptyView.hide()
        materialProgressBar.show()
    }

    override fun hideProgress() {
        materialProgressBar.hide()
        swipeRefresh.isRefreshing = false
        emptyView.setVisibility(moviesAdapter?.itemCount == 0)
    }

    override fun showMoviesList(moviesList: List<Movie>?) {
        moviesAdapter?.updateMoviesList(moviesList)
    }

    override fun addMovieItems(moviesList: List<Movie>?) {
        moviesAdapter?.addMovieItems(moviesList)
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        swipeRefresh.clearAnimation()
        recyclerView.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }
}