package com.ashish.movies.ui.discover.common

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewPresenter
import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW

/**
 * Created by Ashish on Jan 07.
 */
abstract class BaseDiscoverFragment<I : ViewType, P : BaseRecyclerViewPresenter<I, BaseRecyclerViewMvpView<I>>>
    : BaseRecyclerViewFragment<I, BaseRecyclerViewMvpView<I>, P>() {

    companion object {
        const val DISCOVER_MOVIE = 0
        const val DISCOVER_TV_SHOW = 1
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if (getDiscoverMediaType() == DISCOVER_MOVIE) {
            emptyTextView.setText(R.string.no_movies_available)
            emptyImageView.setImageResource(R.drawable.ic_movie_white_100dp)
        } else {
            emptyTextView.setText(R.string.no_tv_shows_available)
            emptyImageView.setImageResource(R.drawable.ic_tv_white_100dp)
        }
    }

    override fun loadData() {
        // no-op
    }

    override fun getAdapterType(): Int {
        if (getDiscoverMediaType() == DISCOVER_MOVIE) {
            return ADAPTER_TYPE_MOVIE
        } else {
            return ADAPTER_TYPE_TV_SHOW
        }
    }

    override fun getTransitionNameId(position: Int): Int {
        if (getDiscoverMediaType() == DISCOVER_MOVIE) {
            return R.string.transition_movie_poster
        } else {
            return R.string.transition_tv_poster
        }
    }

    override fun getDetailIntent(position: Int): Intent? {
        if (getDiscoverMediaType() == DISCOVER_MOVIE) {
            val movie = recyclerViewAdapter.getItem<Movie>(position)
            return MovieDetailActivity.createIntent(activity, movie)
        } else {
            val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
            return TVShowDetailActivity.createIntent(activity, tvShow)
        }
    }

    abstract fun getDiscoverMediaType(): Int

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            val filterBottomSheetDialog = FilterBottomSheetDialogFragment()
            filterBottomSheetDialog.show(childFragmentManager, filterBottomSheetDialog.tag)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}