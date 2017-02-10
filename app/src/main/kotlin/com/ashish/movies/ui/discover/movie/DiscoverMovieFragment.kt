package com.ashish.movies.ui.discover.movie

import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.discover.common.BaseDiscoverFragment

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMovieFragment : BaseDiscoverFragment<Movie, DiscoverMoviePresenter>() {

    companion object {
        fun newInstance() = DiscoverMovieFragment()
    }

    override fun injectDependencies(uiComponent: UiComponent) {
        discoverComponent = uiComponent.discoverComponent()
        discoverComponent.inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_MOVIE
}