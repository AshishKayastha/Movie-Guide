package com.ashish.movies.ui.movies

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.common.BaseFragment
import com.ashish.movies.ui.common.RecyclerViewScrollListener
import com.ashish.movies.ui.widget.ItemOffsetDecoration
import com.ashish.movies.utils.extensions.hide
import com.ashish.movies.utils.extensions.setVisibility
import com.ashish.movies.utils.extensions.show
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

        moviesAdapter = MoviesAdapter()

        recyclerView.apply {
            setHasFixedSize(true)
            emptyView = emptyContentView
            addItemDecoration(ItemOffsetDecoration())
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(scrollListener)
            adapter = moviesAdapter
        }

        swipeRefresh.apply {
            setColorSchemeResources(R.color.colorAccent)
            setSwipeableViews(emptyContentView, recyclerView)
            setOnRefreshListener(this@MoviesFragment)
        }

        presenter.getMovieList(movieType)
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.getMovieList(movieType, showProgress = moviesAdapter?.itemCount == 0)
    }

    override fun showProgress() {
        emptyContentView.hide()
        materialProgressBar.show()
    }

    override fun hideProgress() {
        materialProgressBar.hide()
        swipeRefresh.isRefreshing = false
        emptyContentView.setVisibility(moviesAdapter?.itemCount == 0)
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
        recyclerView.clearOnScrollListeners()
        super.onDestroyView()
    }
}