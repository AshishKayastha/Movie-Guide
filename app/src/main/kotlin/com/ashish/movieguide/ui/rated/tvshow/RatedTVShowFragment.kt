package com.ashish.movieguide.ui.rated.tvshow

import android.content.Intent
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW

class RatedTVShowFragment : BaseRecyclerViewFragment<TVShow,
        BaseRecyclerViewMvpView<TVShow>, RatedTVShowPresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedTVShowFragment()
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedTVShowFragment::class.java,
                RatedTVShowComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun getAdapterType() = ADAPTER_TYPE_TV_SHOW

    override fun getEmptyTextId() = R.string.no_rated_tv_shows_available

    override fun getEmptyImageId() = R.drawable.ic_tv_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_tv_poster

    override fun getDetailIntent(position: Int): Intent? {
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity, tvShow)
    }
}