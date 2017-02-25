package com.ashish.movies.ui.multisearch.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.Movie
import com.ashish.movies.data.models.MultiSearch
import com.ashish.movies.data.models.Person
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.modules.FragmentModule
import com.ashish.movies.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.movie.detail.MovieDetailActivity
import com.ashish.movies.ui.people.detail.PersonDetailActivity
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_MULTI_SEARCH
import com.ashish.movies.utils.Constants.MEDIA_TYPE_MOVIE
import com.ashish.movies.utils.Constants.MEDIA_TYPE_PERSON
import com.ashish.movies.utils.Constants.MEDIA_TYPE_TV

/**
 * Created by Ashish on Jan 05.
 */
class MultiSearchFragment : BaseRecyclerViewFragment<MultiSearch,
        BaseRecyclerViewMvpView<MultiSearch>, MultiSearchPresenter>() {

    companion object {
        fun newInstance() = MultiSearchFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(MultiSearchFragment::class.java,
                MultiSearchFragmentComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout.isEnabled = false
        emptyTextView.setText(R.string.no_results_available)
        emptyImageView.setImageResource(R.drawable.ic_search_white_100dp)
    }

    override fun loadData() {}

    fun searchQuery(query: String) {
        recyclerViewAdapter.clearAll()
        presenter?.setSearchQuery(query)
        presenter?.loadFreshData(null)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MULTI_SEARCH

    override fun getTransitionNameId(position: Int): Int {
        val multiSearch = recyclerViewAdapter.getItem<MultiSearch>(position)
        with(multiSearch) {
            if (mediaType == MEDIA_TYPE_TV) {
                return R.string.transition_tv_poster
            } else if (mediaType == MEDIA_TYPE_PERSON) {
                return R.string.transition_person_profile
            } else {
                return R.string.transition_movie_poster
            }
        }
    }

    override fun getDetailIntent(position: Int): Intent? {
        val multiSearch = recyclerViewAdapter.getItem<MultiSearch>(position)
        with(multiSearch) {
            if (mediaType == MEDIA_TYPE_MOVIE) {
                val movie = Movie(id, title, posterPath = posterPath)
                return MovieDetailActivity.createIntent(activity, movie)

            } else if (mediaType == MEDIA_TYPE_TV) {
                val tvShow = TVShow(id, name, posterPath = posterPath)
                return TVShowDetailActivity.createIntent(activity, tvShow)

            } else if (mediaType == MEDIA_TYPE_PERSON) {
                val people = Person(id, name, profilePath = profilePath)
                return PersonDetailActivity.createIntent(activity, people)

            } else {
                return null
            }
        }
    }
}