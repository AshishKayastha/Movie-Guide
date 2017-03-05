package com.ashish.movieguide.ui.tvshow.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.view.menu.ActionMenuItemView
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Season
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.TVShowDetail
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.tvshow.season.SeasonDetailActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_TV_SHOW
import com.ashish.movieguide.utils.extensions.find
import com.ashish.movieguide.utils.extensions.getBackdropUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.setFavoriteIcon
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.startFavoriteAnimation
import icepick.State

/**
 * Created by Ashish on Jan 03.
 */
class TVShowDetailActivity : FullDetailContentActivity<TVShowDetail, TVShowDetailView, TVShowDetailPresenter>(),
        TVShowDetailView {

    companion object {
        private const val EXTRA_TV_SHOW = "tv_show"

        fun createIntent(context: Context, tvShow: TVShow?): Intent {
            return Intent(context, TVShowDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW, tvShow)
        }
    }

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

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(TVShowDetailActivity::class.java,
                TVShowDetailComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

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

        similarTVShowsViewStub.setOnInflateListener { _, view ->
            val textView = view.find<FontTextView>(R.id.similar_content_title)
            textView.setText(R.string.similar_tv_shows_title)
        }

        inflateViewStubRecyclerView(similarTVShowsViewStub, R.id.similar_content_recycler_view,
                similarTVShowsAdapter!!, similarTVShowList)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> performAction { presenter?.markAsFavorite() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun setFavoriteIcon(isFavorite: Boolean) {
        menu?.setFavoriteIcon(isFavorite)
    }

    override fun animateFavoriteIcon(isFavorite: Boolean) {
        val view = toolbar?.findViewById(R.id.action_favorite)
        if (view is ActionMenuItemView) {
            view.startFavoriteAnimation(isFavorite)
        }
    }

    override fun performCleanup() {
        seasonsAdapter?.removeListener()
        similarTVShowsAdapter?.removeListener()
        super.performCleanup()
    }
}