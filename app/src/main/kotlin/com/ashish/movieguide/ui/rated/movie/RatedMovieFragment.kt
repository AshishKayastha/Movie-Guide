package com.ashish.movieguide.ui.rated.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Movie
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import icepick.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedMovieFragment : BaseRecyclerViewFragment<Movie,
        BaseRecyclerViewMvpView<Movie>, RatedMoviePresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedMovieFragment()
    }

    @Inject lateinit var ratingChangeObserver: RatingChangeObserver

    @JvmField @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedMovieFragment::class.java,
                RatedMovieComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovieRatingChanged()
    }

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getEmptyTextId() = R.string.no_rated_movies_available

    override fun getEmptyImageId() = R.drawable.ic_movie_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val movie = recyclerViewAdapter.getItem<Movie>(position)
        return MovieDetailActivity.createIntent(activity, movie)
    }

    private fun observeMovieRatingChanged() {
        disposable = ratingChangeObserver.getRatingObservable()
                .filter { clickedItemPosition > -1 }
                .filter { (movieId, _) ->
                    val movie = recyclerViewAdapter.getItem<Movie>(clickedItemPosition)
                    return@filter movie.id == movieId
                }
                .subscribe({ (_, rating) ->
                    if (rating > 0) {
                        val movie = recyclerViewAdapter.getItem<Movie>(clickedItemPosition)
                        recyclerViewAdapter.replaceItem(clickedItemPosition, movie.copy(rating = rating))
                    } else {
                        recyclerViewAdapter.removeItem(clickedItemPosition)
                    }
                })
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}