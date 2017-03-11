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
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentStatusObserver
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment.Companion.newInstance
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.FAVORITES
import com.ashish.movieguide.utils.Constants.MEDIA_TYPE_MOVIE
import com.ashish.movieguide.utils.Constants.WATCHLIST
import icepick.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

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

    @Inject lateinit var statusObserver: PersonalContentStatusObserver

    @JvmField @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(PersonalMovieFragment::class.java,
                PersonalMovieComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePersonalContentStatusChange()
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_PERSONAL_MOVIE_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getEmptyTextId() = if (type == FAVORITES) {
        R.string.no_fav_movies_available
    } else {
        R.string.no_movies_watchlist_available
    }

    override fun getEmptyImageId() = R.drawable.ic_movie_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity, movie)
    }

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