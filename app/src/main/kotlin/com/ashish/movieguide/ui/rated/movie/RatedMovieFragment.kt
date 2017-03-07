package com.ashish.movieguide.ui.rated.movie

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE

class RatedMovieFragment : BaseRecyclerViewFragment<Movie,
        BaseRecyclerViewMvpView<Movie>, RatedMoviePresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedMovieFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedMovieFragment::class.java,
                RatedMovieComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getEmptyTextId() = R.string.no_movies_available

    override fun getEmptyImageId() = R.drawable.ic_movie_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity, movie)
    }
}