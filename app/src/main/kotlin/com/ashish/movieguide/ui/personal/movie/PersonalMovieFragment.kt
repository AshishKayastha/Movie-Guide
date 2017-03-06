package com.ashish.movieguide.ui.personal.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment.Companion.newInstance
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.FAVORITES
import com.ashish.movieguide.utils.Constants.WATCHLIST

/**
 * This class will show personal movie lists like favorites and watchlist.
 * It will be determined by movieType passed to [newInstance].
 * The movieType should be either [FAVORITES] or [WATCHLIST]
 */
class PersonalMovieFragment : BaseRecyclerViewFragment<Movie,
        BaseRecyclerViewMvpView<Movie>, PersonalMoviePresenter>() {

    companion object {
        private const val ARG_PERSONAL_MOVIE_TYPE = "personal_movie_type"

        @JvmStatic
        fun newInstance(movieType: Int): PersonalMovieFragment {
            val extras = Bundle()
            extras.putInt(ARG_PERSONAL_MOVIE_TYPE, movieType)
            val fragment = PersonalMovieFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(PersonalMovieFragment::class.java,
                PersonalMovieComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (type == FAVORITES) {
            emptyTextView.setText(R.string.no_fav_movies_available)
        } else {
            emptyTextView.setText(R.string.no_movies_watchlist_available)
        }

        emptyImageView.setImageResource(R.drawable.ic_movie_white_100dp)
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_PERSONAL_MOVIE_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity, movie)
    }
}