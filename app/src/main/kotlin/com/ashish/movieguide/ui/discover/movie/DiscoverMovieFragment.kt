package com.ashish.movieguide.ui.discover.movie

import android.support.v4.app.Fragment
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.ui.discover.base.BaseDiscoverFragment
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

/**
 * Created by Ashish on Jan 06.
 */
class DiscoverMovieFragment : BaseDiscoverFragment<Movie, DiscoverMoviePresenter>() {

    companion object {
        fun newInstance() = DiscoverMovieFragment()
    }

    @Inject lateinit var discoverMoviePresenter: DiscoverMoviePresenter
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = supportFragmentInjector

    override fun providePresenter(): DiscoverMoviePresenter = discoverMoviePresenter

    override fun getDiscoverMediaType() = DISCOVER_MOVIE
}