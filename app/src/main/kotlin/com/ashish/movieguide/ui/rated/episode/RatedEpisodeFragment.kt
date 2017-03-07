package com.ashish.movieguide.ui.rated.episode

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE

class RatedEpisodeFragment : BaseRecyclerViewFragment<Episode,
        BaseRecyclerViewMvpView<Episode>, RatedEpisodePresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedEpisodeFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedEpisodeFragment::class.java,
                RatedEpisodeComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun getAdapterType() = ADAPTER_TYPE_EPISODE

    override fun getEmptyTextId() = R.string.no_episodes_available

    override fun getEmptyImageId() = R.drawable.ic_tv_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_episode_image

    override fun getDetailIntent(position: Int): Intent? {
        val episode = recyclerViewAdapter.getItem<Episode>(position)
        return EpisodeDetailActivity.createIntent(activity, episode.tvShowId, episode)
    }
}