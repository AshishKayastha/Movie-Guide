package com.ashish.movies.ui.movies

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import javax.inject.Inject

/**
 * Created by Ashish on Dec 26.
 */
class MoviesFragment : BaseFragment(), MoviesMvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: MoviesPresenter

    private var moviesAdapter: MoviesAdapter? = null

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun getLayoutId() = R.layout.fragment_movies

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.emptyView = emptyView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        moviesAdapter = MoviesAdapter()
        recyclerView.adapter = moviesAdapter

        swipeRefresh.setSwipeableViews(emptyView)
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {

    }
}