package com.ashish.movieguide.ui.tvshow.list

import android.content.Intent
import android.os.Bundle
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import javax.inject.Inject

/**
 * Created by Ashish on Dec 29.
 */
class TVShowFragment : RecyclerViewFragment<TVShow, RecyclerViewMvpView<TVShow>, TVShowPresenter>() {

    companion object {
        private const val ARG_TV_SHOW_TYPE = "tv_show_type"

        fun newInstance(tvShowType: Int): TVShowFragment {
            val extras = Bundle()
            extras.putInt(ARG_TV_SHOW_TYPE, tvShowType)
            val fragment = TVShowFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    @Inject lateinit var tvShowPresenter: TVShowPresenter

    override fun providePresenter(): TVShowPresenter = tvShowPresenter

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_TV_SHOW_TYPE)
    }

    override fun getEmptyTextId(): Int = R.string.no_tv_shows_available

    override fun getEmptyImageId(): Int = R.drawable.ic_tv_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_TV_SHOW

    override fun getDetailIntent(position: Int): Intent? {
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity!!, tvShow)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_tv_poster
}