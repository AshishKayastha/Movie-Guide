package com.ashish.movies.ui.tvshow.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movies.R
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movies.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW

/**
 * Created by Ashish on Dec 29.
 */
class TVShowFragment : BaseRecyclerViewFragment<TVShow, BaseRecyclerViewMvpView<TVShow>, TVShowPresenter>() {

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

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(TVShowModule(activity)).inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView.setText(R.string.no_tv_shows_available)
        emptyImageView.setImageResource(R.drawable.ic_tv_white_100dp)
    }

    override fun getFragmentArguments() {
        type = arguments.getInt(ARG_TV_SHOW_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_TV_SHOW

    override fun getTransitionNameId(position: Int) = R.string.transition_tv_poster

    override fun getDetailIntent(position: Int): Intent? {
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity, tvShow)
    }
}