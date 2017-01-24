package com.ashish.movies.ui.discover.common

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.discover.common.filter.FilterBottomSheetDialogFragment
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW

/**
 * Created by Ashish on Jan 07.
 */
abstract class BaseDiscoverFragment<I : ViewType, P : BaseDiscoverPresenter<I>>
    : BaseRecyclerViewFragment<I, DiscoverView<I>, P>(), DiscoverView<I> {

    companion object {
        const val DISCOVER_MOVIE = 0
        const val DISCOVER_TV_SHOW = 1
    }

    lateinit var discoverComponent: DiscoverSubComponent

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        swipeRefreshLayout.isEnabled = false

        if (isMovie()) {
            emptyTextView.setText(R.string.no_movies_available)
            emptyImageView.setImageResource(R.drawable.ic_movie_white_100dp)
        } else {
            emptyTextView.setText(R.string.no_tv_shows_available)
            emptyImageView.setImageResource(R.drawable.ic_tv_white_100dp)
        }
    }

    override fun loadData() = presenter?.filterContents()

    override fun getAdapterType(): Int {
        if (isMovie()) {
            return ADAPTER_TYPE_MOVIE
        } else {
            return ADAPTER_TYPE_TV_SHOW
        }
    }

    override fun getTransitionNameId(position: Int): Int {
        if (isMovie()) {
            return R.string.transition_movie_poster
        } else {
            return R.string.transition_tv_poster
        }
    }

    override fun getDetailIntent(position: Int): Intent? {
        if (isMovie()) {
            val movie = recyclerViewAdapter.getItem<Movie>(position)
            return MovieDetailActivity.createIntent(activity, movie)
        } else {
            val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
            return TVShowDetailActivity.createIntent(activity, tvShow)
        }
    }

    abstract fun getDiscoverMediaType(): Int

    private fun isMovie() = getDiscoverMediaType() == DISCOVER_MOVIE

    override fun showFilterBottomSheetDialog(filterQuery: FilterQuery) {
        val filterBottomSheetFragment = FilterBottomSheetDialogFragment.newInstance(isMovie(), filterQuery)
        filterBottomSheetFragment.setTargetFragment(this, 1001)
        filterBottomSheetFragment.show(childFragmentManager, filterBottomSheetFragment.tag)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            presenter?.onFilterMenuItemClick()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}