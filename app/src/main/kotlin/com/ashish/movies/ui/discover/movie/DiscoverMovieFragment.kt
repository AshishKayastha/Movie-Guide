package com.ashish.movies.ui.discover.movie

import com.ashish.movies.data.models.Movie
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movies.ui.discover.base.BaseDiscoverFragment

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMovieFragment : BaseDiscoverFragment<Movie, DiscoverMoviePresenter>() {

    companion object {
        fun newInstance() = DiscoverMovieFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(DiscoverMovieFragment::class.java,
                DiscoverMovieComponent.Builder::class.java)
                .build()
                .inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_MOVIE
}