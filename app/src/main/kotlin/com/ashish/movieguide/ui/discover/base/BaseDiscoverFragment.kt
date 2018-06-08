package com.ashish.movieguide.ui.discover.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.FilterQuery
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.discover.filter.FilterBottomSheetDialogFragment
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movieguide.utils.extensions.performAction
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 07.
 */
abstract class BaseDiscoverFragment<I : ViewType, P : BaseDiscoverPresenter<I>>
    : RecyclerViewFragment<I, DiscoverView<I>, P>(), DiscoverView<I>, HasSupportFragmentInjector {

    companion object {
        const val DISCOVER_MOVIE = 0
        const val DISCOVER_TV_SHOW = 1
        private const val RC_FILTER_FRAGMENT = 1001
    }

    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        swipeRefresh.isEnabled = false
    }

    override fun supportFragmentInjector() = supportFragmentInjector

    override fun loadData() = presenter.filterContents()

    override fun getEmptyTextId(): Int {
        return if (isMovie()) R.string.no_movies_available else R.string.no_tv_shows_available
    }

    override fun getEmptyImageId(): Int {
        return if (isMovie()) R.drawable.ic_movie_white_100dp else R.drawable.ic_tv_white_100dp
    }

    override fun getAdapterType(): Int {
        return if (isMovie()) ADAPTER_TYPE_MOVIE else ADAPTER_TYPE_TV_SHOW
    }

    override fun getDetailIntent(position: Int): Intent? {
        return if (isMovie()) {
            val movie = recyclerViewAdapter.getItem<Movie>(position)
            MovieDetailActivity.createIntent(activity!!, movie)
        } else {
            val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
            TVShowDetailActivity.createIntent(activity!!, tvShow)
        }
    }

    override fun getTransitionNameId(position: Int): Int {
        return if (isMovie()) R.string.transition_movie_poster else R.string.transition_tv_poster
    }

    abstract fun getDiscoverMediaType(): Int

    private fun isMovie() = getDiscoverMediaType() == DISCOVER_MOVIE

    override fun clearFilteredData() = recyclerViewAdapter.clearAll()

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> performAction { presenter?.onFilterMenuItemClick() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun showFilterBottomSheetDialog(filterQuery: FilterQuery) {
        val filterBottomSheetFragment = FilterBottomSheetDialogFragment.newInstance(isMovie(), filterQuery)
        filterBottomSheetFragment.setTargetFragment(this, RC_FILTER_FRAGMENT)
        filterBottomSheetFragment.show(fragmentManager, filterBottomSheetFragment.tag)
    }
}