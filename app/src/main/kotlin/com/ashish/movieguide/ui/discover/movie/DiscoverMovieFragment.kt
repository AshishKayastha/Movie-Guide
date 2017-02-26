package com.ashish.movieguide.ui.discover.movie

import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.discover.base.BaseDiscoverFragment

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