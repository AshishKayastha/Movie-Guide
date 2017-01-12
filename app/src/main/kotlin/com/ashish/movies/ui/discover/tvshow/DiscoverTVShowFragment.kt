package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.discover.common.BaseDiscoverFragment
import com.ashish.movies.ui.discover.common.DiscoverModule

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowFragment : BaseDiscoverFragment<TVShow, DiscoverTVShowPresenter>() {

    companion object {
        fun newInstance() = DiscoverTVShowFragment()
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(DiscoverModule(activity)).inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_TV_SHOW
}