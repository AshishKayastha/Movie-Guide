package com.ashish.movieguide.ui.discover.tvshow

import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.discover.base.BaseDiscoverFragment

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowFragment : BaseDiscoverFragment<TVShow, DiscoverTVShowPresenter>() {

    companion object {
        fun newInstance() = DiscoverTVShowFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(DiscoverTVShowFragment::class.java,
                DiscoverTVShowComponent.Builder::class.java)
                .build()
                .inject(this)
    }

    override fun getDiscoverMediaType() = DISCOVER_TV_SHOW
}