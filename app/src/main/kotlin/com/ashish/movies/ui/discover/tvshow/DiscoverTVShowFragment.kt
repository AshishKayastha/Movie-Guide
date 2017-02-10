package com.ashish.movies.ui.discover.tvshow

import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.discover.common.BaseDiscoverFragment

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowFragment : BaseDiscoverFragment<TVShow, DiscoverTVShowPresenter>() {

    companion object {
        fun newInstance() = DiscoverTVShowFragment()
    }

    override fun injectDependencies(uiComponent: UiComponent) {
        discoverComponent = uiComponent.discoverComponent()
        discoverComponent.inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_TV_SHOW
}