package com.ashish.movies.ui.discover.movie

import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.discover.common.BaseDiscoverFragment
import com.ashish.movies.ui.discover.common.DiscoverModule

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMovieFragment : BaseDiscoverFragment<Movie, DiscoverMoviePresenter>() {

    companion object {
        fun newInstance() = DiscoverMovieFragment()
    }

    override fun injectDependencies(appComponent: AppComponent) {
        discoverComponent = appComponent.plus(DiscoverModule(activity))
        discoverComponent.inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_MOVIE
}