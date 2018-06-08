package com.ashish.movieguide.ui.personal.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.TVShow
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.RecyclerViewMvpView
import com.ashish.movieguide.ui.common.personalcontent.PersonalContentStatusObserver
import com.ashish.movieguide.ui.personal.movie.PersonalMovieFragment.Companion.newInstance
import com.ashish.movieguide.ui.personal.tvshow.PersonalTVShowFragment.Companion.newInstance
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movieguide.utils.TMDbConstants.FAVORITES
import com.ashish.movieguide.utils.TMDbConstants.MEDIA_TYPE_TV
import com.ashish.movieguide.utils.TMDbConstants.WATCHLIST
import com.evernote.android.state.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * This class will show personal tv show lists like favorites and watchlist.
 * It will be determined by tvShowType passed to [newInstance].
 * The tvShowType should be either [FAVORITES] or [WATCHLIST]
 */
class PersonalTVShowFragment : RecyclerViewFragment<TVShow, RecyclerViewMvpView<TVShow>, PersonalTVShowPresenter>() {

    companion object {
        private const val ARG_PERSONAL_TV_SHOW_TYPE = "personal_tv_show_type"

        fun newInstance(tvShowType: Int): PersonalTVShowFragment {
            val extras = Bundle()
            extras.putInt(ARG_PERSONAL_TV_SHOW_TYPE, tvShowType)
            val fragment = PersonalTVShowFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    @Inject lateinit var statusObserver: PersonalContentStatusObserver
    @Inject lateinit var personalTvPresenter: PersonalTVShowPresenter

    @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePersonalContentStatusChange()
    }

    override fun providePresenter(): PersonalTVShowPresenter = personalTvPresenter

    override fun getFragmentArguments(arguments: Bundle?) {
        type = arguments?.getInt(ARG_PERSONAL_TV_SHOW_TYPE)
    }

    override fun getEmptyTextId(): Int = if (type == FAVORITES) {
        R.string.no_fav_tv_shows_available
    } else {
        R.string.no_tv_shows_watchlist_available
    }

    override fun getEmptyImageId(): Int = R.drawable.ic_tv_white_100dp

    override fun getAdapterType(): Int = ADAPTER_TYPE_TV_SHOW

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity!!, tvShow)
    }

    override fun getTransitionNameId(position: Int): Int = R.string.transition_tv_poster

    private fun observePersonalContentStatusChange() {
        disposable = statusObserver.getContentStatusObservable()
                .filter { (_, mediaType) -> mediaType == MEDIA_TYPE_TV && clickedItemPosition > -1 }
                .filter { (tvId) ->
                    // Filter tv show that has same id as that of currently removed
                    // tv show item to avoid wrong data to be removed from list.
                    val tvShow = recyclerViewAdapter.getItem<TVShow>(clickedItemPosition)
                    return@filter tvShow.id == tvId
                }
                .subscribe { recyclerViewAdapter.removeItem(clickedItemPosition) }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}