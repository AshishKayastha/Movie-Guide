package com.ashish.movieguide.ui.rated.episode

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.remote.entities.tmdb.Episode
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.evernote.android.state.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedEpisodeFragment : RecyclerViewFragment<Episode, RecyclerViewMvpView<Episode>, RatedEpisodePresenter>() {

    companion object {
        fun newInstance() = RatedEpisodeFragment()
    }

    @Inject lateinit var ratingChangeObserver: RatingChangeObserver
    @Inject lateinit var ratedEpisodePresenter: RatedEpisodePresenter

    @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEpisodeRatingChanged()
    }

    override fun providePresenter(): RatedEpisodePresenter = ratedEpisodePresenter

    override fun getEmptyTextId(): Int = R.string.no_rated_episodes_available

    override fun getEmptyImageId(): Int = R.drawable.ic_tv_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_EPISODE

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val episode = recyclerViewAdapter.getItem<Episode>(position)
        return EpisodeDetailActivity.createIntent(activity!!, episode.tvShowId, episode)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_episode_image

    private fun observeEpisodeRatingChanged() {
        disposable = ratingChangeObserver.getRatingObservable()
                .filter { clickedItemPosition > -1 }
                .filter { (episodeId, _) ->
                    val episode = recyclerViewAdapter.getItem<Episode>(clickedItemPosition)
                    return@filter episode.id == episodeId
                }
                .subscribe({ (_, rating) ->
                    if (rating > 0) {
                        val episode = recyclerViewAdapter.getItem<Episode>(clickedItemPosition)
                        recyclerViewAdapter.replaceItem(clickedItemPosition,
                                episode.copy(rating = rating.toDouble()))
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