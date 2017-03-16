package com.ashish.movieguide.ui.rated.episode

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Episode
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import icepick.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedEpisodeFragment : BaseRecyclerViewFragment<Episode,
        BaseRecyclerViewMvpView<Episode>, RatedEpisodePresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedEpisodeFragment()
    }

    @Inject lateinit var ratingChangeObserver: RatingChangeObserver

    @JvmField @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedEpisodeFragment::class.java,
                RatedEpisodeComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEpisodeRatingChanged()
    }

    override fun getAdapterType() = ADAPTER_TYPE_EPISODE

    override fun getEmptyTextId() = R.string.no_rated_episodes_available

    override fun getEmptyImageId() = R.drawable.ic_tv_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_episode_image

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val episode = recyclerViewAdapter.getItem<Episode>(position)
        return EpisodeDetailActivity.createIntent(activity, episode.tvShowId, episode)
    }

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