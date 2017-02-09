package com.ashish.movies.ui.tvshow.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Season
import com.ashish.movies.data.models.TVShow
import com.ashish.movies.data.models.TVShowDetail
import com.ashish.movies.di.components.UiComponent
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.tvshow.season.SeasonDetailActivity
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.ApiConstants.MEDIA_TYPE_TV
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movies.utils.extensions.find
import com.ashish.movies.utils.extensions.getBackdropUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.isNotNullOrEmpty
import com.ashish.movies.utils.extensions.setTitleAndYear
import icepick.State

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailActivity : FullDetailContentActivity<TVShowDetail, TVShowDetailView, TVShowDetailPresenter>(),
        TVShowDetailView {

    @JvmField @State var tvShow: TVShow? = null

    private val seasonsViewStub: ViewStub by bindView(R.id.seasons_view_stub)
    private val similarTVShowsViewStub: ViewStub by bindView(R.id.similar_content_view_stub)

    private var seasonsAdapter: RecyclerViewAdapter<Season>? = null
    private var similarTVShowsAdapter: RecyclerViewAdapter<TVShow>? = null

    private val onSeasonItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val season = seasonsAdapter?.getItem<Season>(position)
            val intent = SeasonDetailActivity.createIntent(this@TVShowDetailActivity, tvShow?.id, season)
            startNewActivityWithTransition(view, R.string.transition_season_poster, intent)
        }
    }

    private val onSimilarTVShowItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val tvShow = similarTVShowsAdapter?.getItem<TVShow>(position)
            val intent = TVShowDetailActivity.createIntent(this@TVShowDetailActivity, tvShow)
            startNewActivityWithTransition(view, R.string.transition_tv_poster, intent)
        }
    }

    companion object {
        private const val EXTRA_TV_SHOW = "tv_show"

        fun createIntent(context: Context, tvShow: TVShow?): Intent {
            return Intent(context, TVShowDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW, tvShow)
        }
    }

    override fun injectDependencies(uiComponent: UiComponent) = uiComponent.inject(this)

    override fun getLayoutId() = R.layout.acivity_detail_tv_show

    override fun getIntentExtras(extras: Bundle?) {
        tvShow = extras?.getParcelable(EXTRA_TV_SHOW)
    }

    override fun getTransitionNameId() = R.string.transition_tv_poster

    override fun loadDetailContent() {
        presenter?.loadDetailContent(tvShow?.id)
    }

    override fun getBackdropPath() = tvShow?.backdropPath.getBackdropUrl()

    override fun getPosterPath() = tvShow?.posterPath.getPosterUrl()

    override fun showDetailContent(detailContent: TVShowDetail) {
        detailContent.apply {
            if (getBackdropPath().isNullOrEmpty() && backdropPath.isNotNullOrEmpty()) {
                showBackdropImage(backdropPath.getBackdropUrl())
            }

            imdbId = detailContent.externalIds?.imdbId
            titleText.setTitleAndYear(name, firstAirDate)
        }
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_TV_SHOW

    override fun getItemTitle(): String = tvShow?.name ?: ""

    override fun showSeasonsList(seasonsList: List<Season>) {
        seasonsAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_SEASON,
                onSeasonItemClickLitener)

        inflateViewStubRecyclerView(seasonsViewStub, R.id.seasons_recycler_view, seasonsAdapter!!, seasonsList)
    }

    override fun showSimilarTVShowList(similarTVShowList: List<TVShow>) {
        similarTVShowsAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_TV_SHOW,
                onSimilarTVShowItemClickLitener)

        similarTVShowsViewStub.setOnInflateListener { viewStub, view ->
            val textView = view.find<FontTextView>(R.id.similar_content_title)
            textView.setText(R.string.similar_tv_shows_title)
        }

        inflateViewStubRecyclerView(similarTVShowsViewStub, R.id.similar_content_recycler_view,
                similarTVShowsAdapter!!, similarTVShowList)
    }

    override fun getMediaType() = MEDIA_TYPE_TV

    override fun performCleanup() {
        seasonsAdapter?.removeListener()
        similarTVShowsAdapter?.removeListener()
        super.performCleanup()
    }
}