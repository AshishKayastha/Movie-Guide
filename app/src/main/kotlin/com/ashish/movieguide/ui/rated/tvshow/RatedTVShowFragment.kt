package com.ashish.movieguide.ui.rated.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.di.modules.FragmentModule
import com.ashish.movieguide.di.multibindings.fragment.FragmentComponentBuilderHost
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewFragment
import com.ashish.movieguide.ui.base.recyclerview.BaseRecyclerViewMvpView
import com.ashish.movieguide.ui.common.rating.RatingChangeObserver
import com.ashish.movieguide.ui.tvshow.detail.TVShowDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import icepick.State
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RatedTVShowFragment : BaseRecyclerViewFragment<TVShow,
        BaseRecyclerViewMvpView<TVShow>, RatedTVShowPresenter>() {

    companion object {
        @JvmStatic fun newInstance() = RatedTVShowFragment()
    }

    @Inject lateinit var ratingChangeObserver: RatingChangeObserver

    @JvmField @State var clickedItemPosition: Int = -1

    private var disposable: Disposable? = null

    override fun injectDependencies(builderHost: FragmentComponentBuilderHost) {
        builderHost.getFragmentComponentBuilder(RatedTVShowFragment::class.java,
                RatedTVShowComponent.Builder::class.java)
                .withModule(FragmentModule(activity))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTVRatingChanged()
    }

    override fun getAdapterType() = ADAPTER_TYPE_TV_SHOW

    override fun getEmptyTextId() = R.string.no_rated_tv_shows_available

    override fun getEmptyImageId() = R.drawable.ic_tv_white_100dp

    override fun getTransitionNameId(position: Int) = R.string.transition_tv_poster

    override fun getDetailIntent(position: Int): Intent? {
        clickedItemPosition = position
        val tvShow = recyclerViewAdapter.getItem<TVShow>(position)
        return TVShowDetailActivity.createIntent(activity, tvShow)
    }

    private fun observeTVRatingChanged() {
        disposable = ratingChangeObserver.getRatingObservable()
                .filter { clickedItemPosition > -1 }
                .filter { (tvId, _) ->
                    val tvShow = recyclerViewAdapter.getItem<TVShow>(clickedItemPosition)
                    return@filter tvShow.id == tvId
                }
                .subscribe({ (_, rating) ->
                    if (rating > 0) {
                        val tvShow = recyclerViewAdapter.getItem<TVShow>(clickedItemPosition)
                        recyclerViewAdapter.replaceItem(clickedItemPosition, tvShow.copy(rating = rating))
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