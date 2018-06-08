package com.ashish.movieguide.ui.personal.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentStatusObserver
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment.Companion.newInstance
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.TMDbConstants.FAVORITES
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.TMDbConstants.WATCHLIST
import com.evernote.android.state.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * This class will show personal movie lists like favorites and watchlist.
 * It will be determined by movieType passed to [newInstance].
 * The movieType should be either [FAVORITES] or [WATCHLIST]
 */
class PersonalMovieFragment : RecyclerViewFragment<Movie, RecyclerViewMvpView<Movie>, PersonalMoviePresenter>() {

    companion object {
        private const val ARG_PERSONAL_MOVIE_TYPE = "personal_movie_type"

        fun newInstance(movieType: Int): PersonalMovieFragment {
            val extras = Bundle()
            extras.putInt(ARG_PERSONAL_MOVIE_TYPE, movieType)
            val fragment = PersonalMovieFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    @Inject lateinit var statusObserver: PersonalContentStatusObserver
    @Inject lateinit var personalMoviePresenter: PersonalMoviePresenter

    @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePersonalContentStatusChange()
    }

    override fun providePresenter(): PersonalMoviePresenter = personalMoviePresenter

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_PERSONAL_MOVIE_TYPE)
    }

    override fun getEmptyTextId(): Int = if (type == FAVORITES) {
        R.string.no_fav_movies_available
    } else {
        R.string.no_movies_watchlist_available
    }

    override fun getEmptyImageId(): Int = R.drawable.ic_movie_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_MOVIE

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity!!, movie)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_movie_poster

    private fun observePersonalContentStatusChange() {
        disposable = statusObserver.getContentStatusObservable()
                .filter { (_, mediaType) -> mediaType == MEDIA_TYPE_MOVIE && clickedItemPosition > -1 }
                .filter { (movieId) ->
                    // Filter movie that has same id as that of currently removed
                    // movie item to avoid wrong data to be removed from list.
                    val movie = recyclerViewAdapter.getItem<Movie>(clickedItemPosition)
                    return@filter movie.id == movieId
                }
                .subscribe { recyclerViewAdapter.removeItem(clickedItemPosition) }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}