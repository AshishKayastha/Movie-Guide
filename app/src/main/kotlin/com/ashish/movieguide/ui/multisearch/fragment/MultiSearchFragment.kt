package com.ashish.movieguide.ui.multisearch.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.data.network.entities.tmdb.MultiSearch
import com.ashish.movieguide.data.network.entities.tmdb.Person
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.people.detail.PersonDetailActivity
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MULTI_SEARCH
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_PERSON
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_TV
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchFragment : BaseRecyclerViewFragment<MultiSearch,
        BaseRecyclerViewMvpView<MultiSearch>, MultiSearchPresenter>() {

    @Inject lateinit var multiSearchPresenter: MultiSearchPresenter

    companion object {
        fun newInstance() = MultiSearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.isEnabled = false
    }

    override fun providePresenter(): MultiSearchPresenter = multiSearchPresenter

    override fun loadData() {
        // no-op
    }

    fun searchQuery(query: String) {
        recyclerViewAdapter.clearAll()
        multiSearchPresenter.setSearchQuery(query)
        multiSearchPresenter.loadFreshData(null)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MULTI_SEARCH

    override fun getEmptyTextId() = R.string.no_results_available

    override fun getEmptyImageId() = R.drawable.ic_search_white_100dp

    override fun getTransitionNameId(position: Int): Int {
        val multiSearch = recyclerViewAdapter.getItem<MultiSearch>(position)
        with(multiSearch) {
            return when (mediaType) {
                MEDIA_TYPE_TV -> R.string.transition_tv_poster
                MEDIA_TYPE_PERSON -> R.string.transition_person_profile
                else -> R.string.transition_movie_poster
            }
        }
    }

    override fun getDetailIntent(position: Int): Intent? {
        if (activity != null) {
            val multiSearch = recyclerViewAdapter.getItem<MultiSearch>(position)
            with(multiSearch) {
                return when (mediaType) {
                    MEDIA_TYPE_MOVIE -> {
                        val movie = Movie(id, title, posterPath = posterPath)
                        MovieDetailActivity.createIntent(activity!!, movie)
                    }

                    MEDIA_TYPE_TV -> {
                        val tvShow = TVShow(id, name, posterPath = posterPath)
                        TVShowDetailActivity.createIntent(activity!!, tvShow)
                    }

                    MEDIA_TYPE_PERSON -> {
                        val people = Person(id, name, profilePath = profilePath)
                        PersonDetailActivity.createIntent(activity!!, people)
                    }

                    else -> null
                }
            }
        } else {
            return null
        }
    }
}