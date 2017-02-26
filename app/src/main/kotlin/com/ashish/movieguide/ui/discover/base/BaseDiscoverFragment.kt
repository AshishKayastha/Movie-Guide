package com.ashish.movieguide.ui.discover.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.FilterQuery
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilder
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.discover.filter.FilterBottomSheetDialogFragment
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Jan 07.
 */
abstract class BaseDiscoverFragment<I : ViewType, P : BaseDiscoverPresenter<I>>
    : BaseRecyclerViewFragment<I, DiscoverView<I>, P>(), DiscoverView<I>, FragmentComponentBuilderHost {

    companion object {
        const val DISCOVER_MOVIE = 0
        const val DISCOVER_TV_SHOW = 1
    }

    @Inject
    lateinit var componentBuilders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<FragmentComponentBuilder<*, *>>>

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

    override fun <F : Fragment, B : FragmentComponentBuilder<F, AbstractComponent<F>>>
            getFragmentComponentBuilder(fragmentKey: Class<F>, builderType: Class<B>): B {
        return builderType.cast(componentBuilders[fragmentKey]!!.get())
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

    override fun clearFilteredData() = recyclerViewAdapter.clearAll()

    override fun showFilterBottomSheetDialog(filterQuery: FilterQuery) {
        val filterBottomSheetFragment = FilterBottomSheetDialogFragment.newInstance(isMovie(), filterQuery)
        filterBottomSheetFragment.setTargetFragment(this, 1001)
        filterBottomSheetFragment.show(childFragmentManager, filterBottomSheetFragment.tag)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> performAction { presenter?.onFilterMenuItemClick() }
        else -> super.onOptionsItemSelected(item)
    }
}