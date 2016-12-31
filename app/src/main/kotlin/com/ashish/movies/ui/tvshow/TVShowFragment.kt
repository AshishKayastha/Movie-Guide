package com.ashish.movies.ui.tvshow

import android.os.Bundle
import android.view.View
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.common.RecyclerViewScrollListener
import com.ashish.movies.ui.movie.TVShowModule

/**
 * Created by Ashish on Dec 29.
 */
class TVShowFragment : BaseRecyclerViewFragment<TVShow, TVShowMvpView, TVShowPresenter>(), TVShowMvpView {

    private var tvShowType: Int? = null

    companion object {
        const val ARG_TV_SHOW_TYPE = "tv_show_type"

        const val POPULAR_TV_SHOWS = 1
        const val TOP_RATED_TV_SHOWS = 2
        const val AIRING_TODAY_TV_SHOWS = 3

        fun newInstance(tvShowType: Int): TVShowFragment {
            val extras = Bundle()
            extras.putInt(ARG_TV_SHOW_TYPE, tvShowType)
            val fragment = TVShowFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    private val scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
        override fun onLoadMore(currentPage: Int) {
            if (currentPage > 1) presenter.loadMoreTVShows(tvShowType, currentPage)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(TVShowModule()).inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addOnScrollListener(scrollListener)
        presenter.getTVShowList(tvShowType)
    }

    override fun initView() {
        tvShowType = arguments.getInt(ARG_TV_SHOW_TYPE)
        recyclerViewAdapter = TVShowAdapter()
    }

    override fun onRefresh() {
        scrollListener.resetPageCount()
        presenter.getTVShowList(tvShowType, showProgress = recyclerViewAdapter.itemCount == 0)
    }
}