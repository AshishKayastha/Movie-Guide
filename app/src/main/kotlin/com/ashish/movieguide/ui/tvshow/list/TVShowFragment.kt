package com.ashish.movieguide.ui.tvshow.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW

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

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(TVShowFragment::class.java, TVShowComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTextView.setText(R.string.no_tv_shows_available)
        emptyImageView.setImageResource(R.drawable.ic_tv_white_100dp)
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_TV_SHOW_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_TV_SHOW

    override fun getTransitionNameId(position: Int) = R.string.transition_tv_poster

    override fun getDetailIntent(position: Int): Intent? {
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity, tvShow)
    }
}