package com.ashish.movies.ui.tvshow.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter.Companion.ADAPTER_TYPE_TV_SHOW
import com.ashish.movies.utils.Constants
import com.ashish.movies.utils.Constants.NOT_AVAILABLE
import com.ashish.movies.utils.extensions.getFormattedGenres
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTitleAndYear

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailActivity : BaseDetailActivity<TVShow, BaseDetailMvpView<TVShow>, TVShowDetailPresenter>() {

    val similarTVShowsViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private lateinit var similarTVShowsAdapter: RecyclerViewAdapter<TVShow>

    private val onSimilarTVShowItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val tvShow = similarTVShowsAdapter.getItem<TVShow>(position)
            val intent = TVShowDetailActivity.createIntent(this@TVShowDetailActivity, tvShow)
            startActivityWithTransition(R.string.transition_poster_image, view, intent)
        }
    }

    companion object {
        const val EXTRA_TV_SHOW = "tv_show"

        fun createIntent(context: Context, tvShow: TVShow?): Intent {
            return Intent(context, TVShowDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW, tvShow)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(TVShowDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_movie_detail

    override fun getIntentExtras(extras: Bundle?) {
        item = extras?.getParcelable(EXTRA_TV_SHOW)
    }

    override fun loadDetailContent() = presenter.loadDetailContent(item?.id)

    override fun getBackdropPath(): String {
        val backdropPath = item?.backdropPath
        return if (backdropPath.isNotNullOrEmpty()) Constants.BACKDROP_W780_URL_PREFIX + backdropPath else ""
    }

    override fun getPosterPath(): String {
        val posterPath = item?.posterPath
        return if (posterPath.isNotNullOrEmpty()) Constants.POSTER_W500_URL_PREFIX + posterPath else ""
    }

    override fun showDetailContent(item: TVShow?) {
        item?.apply {
            titleText.setTitleAndYear(name, firstAirDate)
            overviewText.text = overview ?: NOT_AVAILABLE
            genresText.text = genres.getFormattedGenres()
            statusText.text = status ?: NOT_AVAILABLE
            releaseDateText.text = firstAirDate.getFormattedReleaseDate(this@TVShowDetailActivity)
        }
        super.showDetailContent(item)
    }

    override fun getItemTitle(): String = item?.name ?: ""

    override fun showSimilarContentList(similarItemList: List<TVShow>) {
        similarTVShowsAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_TV_SHOW,
                onSimilarTVShowItemClickLitener)

        inflateViewStubRecyclerView(similarTVShowsViewStub, R.id.similar_content_recycler_view,
                similarTVShowsAdapter, similarItemList)
    }
}