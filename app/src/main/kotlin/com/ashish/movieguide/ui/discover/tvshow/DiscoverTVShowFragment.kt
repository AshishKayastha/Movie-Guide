package com.ashish.movieguide.ui.discover.tvshow

import android.support.v4.app.Fragment
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.discover.base.BaseDiscoverFragment
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverTVShowFragment : BaseDiscoverFragment<TVShow, DiscoverTVShowPresenter>() {

    companion object {
        fun newInstance() = DiscoverTVShowFragment()
    }

    @Inject lateinit var discoverTvPresenter: DiscoverTVShowPresenter
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = supportFragmentInjector

    override fun providePresenter(): DiscoverTVShowPresenter = discoverTvPresenter

    override fun getDiscoverMediaType() = DISCOVER_TV_SHOW
}