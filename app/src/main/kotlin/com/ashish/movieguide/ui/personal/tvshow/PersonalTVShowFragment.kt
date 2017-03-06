package com.ashish.movieguide.ui.personal.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment.Companion.newInstance
import com.ashish.movieguide.ui.personal.tvshow.PersonalTVShowFragment.Companion.newInstance
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movieguide.utils.Constants.FAVORITES
import com.ashish.movieguide.utils.Constants.WATCHLIST

/**
 * This class will show personal tv show lists like favorites and watchlist.
 * It will be determined by tvShowType passed to [newInstance].
 * The tvShowType should be either [FAVORITES] or [WATCHLIST]
 */
class PersonalTVShowFragment : BaseRecyclerViewFragment<TVShow,
        BaseRecyclerViewMvpView<TVShow>, PersonalTVShowPresenter>() {

    companion object {
        private const val ARG_PERSONAL_TV_SHOW_TYPE = "personal_tv_show_type"

        @JvmStatic
        fun newInstance(tvShowType: Int): PersonalTVShowFragment {
            val extras = Bundle()
            extras.putInt(ARG_PERSONAL_TV_SHOW_TYPE, tvShowType)
            val fragment = PersonalTVShowFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(PersonalTVShowFragment::class.java,
                PersonalTVShowComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (type == FAVORITES) {
            emptyTextView.setText(R.string.no_fav_tv_shows_available)
        } else {
            emptyTextView.setText(R.string.no_tv_shows_watchlist_available)
        }

        emptyImageView.setImageResource(R.drawable.ic_tv_white_100dp)
    }

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_PERSONAL_TV_SHOW_TYPE)
    }

    override fun getAdapterType() = ADAPTER_TYPE_TV_SHOW

    override fun getTransitionNameId(position: Int) = R.string.transition_tv_poster

    override fun getDetailIntent(position: Int): Intent? {
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity, tvShow)
    }
}