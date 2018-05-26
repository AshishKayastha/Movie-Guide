package com.ashish.movieguide.ui.rated.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Movie
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.movie.detail.MovieDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_MOVIE
import com.evernote.android.state.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedMovieFragment : BaseRecyclerViewFragment<Movie,
        BaseRecyclerViewMvpView<Movie>, RatedMoviePresenter>() {

    companion object {
        fun newInstance() = RatedMovieFragment()
    }

    @Inject lateinit var ratedMoviePresenter: RatedMoviePresenter
    @Inject lateinit var ratingChangeObserver: RatingChangeObserver

    @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMovieRatingChanged()
    }

    override fun providePresenter(): RatedMoviePresenter = ratedMoviePresenter

    override fun getAdapterType() = ADAPTER_TYPE_MOVIE

    override fun getEmptyTextId() = R.string.no_rated_movies_available

    override fun getEmptyImageId() = R.drawable.ic_movie_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_movie_poster

    override fun getDetailIntent(position: Int): Intent? {
        return if (activity != null) {
            clickedItemPosition = position
            val movie = recyclerViewAdapter.getItem<Movie>(position)
            MovieDetailActivity.createIntent(activity!!, movie)
        } else null
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
                        recyclerViewAdapter.replaceItem(clickedItemPosition,
                                movie.copy(rating = rating.toDouble()))
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