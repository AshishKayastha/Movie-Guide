package com.ashish.movies.ui.tvshow

import android.os.Bundle
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.recyclerview.BaseRecyclerViewFragment

/**
 * Created by Ashish on Dec 29.
 */
class TVShowFragment : BaseRecyclerViewFragment<TVShow, TVShowMvpView, TVShowPresenter>(), TVShowMvpView {

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
        appComponent.plus(TVShowModule()).inject(this)
    }

    override fun initView() {
        type = arguments.getInt(ARG_TV_SHOW_TYPE)
        recyclerViewAdapter = TVShowAdapter()
    }
}