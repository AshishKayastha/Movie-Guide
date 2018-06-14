package com.ashish.movieguide.ui.rated.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.evernote.android.state.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedTVShowFragment : RecyclerViewFragment<TVShow, RecyclerViewMvpView<TVShow>, RatedTVShowPresenter>() {

    companion object {
        fun newInstance() = RatedTVShowFragment()
    }

    @Inject lateinit var ratedTvPresenter: RatedTVShowPresenter
    @Inject lateinit var ratingChangeObserver: RatingChangeObserver

    @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTVRatingChanged()
    }

    override fun providePresenter(): RatedTVShowPresenter = ratedTvPresenter

    override fun getEmptyTextId(): Int = R.string.no_rated_tv_shows_available

    override fun getEmptyImageId(): Int = R.drawable.ic_tv_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_TV_SHOW

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity!!, tvShow)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_tv_poster

    private fun observeTVRatingChanged() {
        disposable = ratingChangeObserver.getRatingObservable()
                .filter { clickedItemPosition > -1 }
                .filter { (tvId, _) ->
                    val tvShow = recyclerViewAdapter.getItem<TVShow>(clickedItemPosition)
                    return@filter tvShow.id == tvId
                }
                .subscribe({ (_, rating) ->
                    if (rating > 0) {
                        val tvShow = recyclerViewAdapter.getItem<TVShow>(clickedItemPosition)
                        recyclerViewAdapter.replaceItem(
                                clickedItemPosition,
                                tvShow.copy(rating = rating.toDouble())
                        )
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