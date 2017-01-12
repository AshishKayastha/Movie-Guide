package com.ashish.movies.ui.tvshow.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.data.models.TVShowSeason
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.FullDetailContentActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.tvshow.episode.EpisodeDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.setTitleAndYear
import com.ashish.movies.utils.extensions.setTransitionName

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailActivity : FullDetailContentActivity<SeasonDetail, SeasonDetailView, SeasonDetailPresenter>(),
        SeasonDetailView {

    private val episodesViewStub: ViewStub by bindView(R.id.episodes_view_stub)

    private var tvShowId: Long? = null
    private var tvShowSeason: TVShowSeason? = null
    private var episodesAdapter: RecyclerViewAdapter<Episode>? = null

    private val onEpisodeItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val episode = episodesAdapter?.getItem<Episode>(position)
            val intent = EpisodeDetailActivity.createIntent(this@SeasonDetailActivity, tvShowId, episode)
            startActivityWithTransition(view, R.string.transition_episode_image, intent)
        }
    }

    companion object {
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"
        private const val EXTRA_TV_SHOW_SEASON = "tv_show_season"

        fun createIntent(context: Context, tvShowId: Long?, tvShowSeason: TVShowSeason?): Intent {
            return Intent(context, SeasonDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_TV_SHOW_SEASON, tvShowSeason)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(SeasonDetailModule(this)).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_season

    override fun getIntentExtras(extras: Bundle?) {
        tvShowId = extras?.getLong(EXTRA_TV_SHOW_ID)
        tvShowSeason = extras?.getParcelable(EXTRA_TV_SHOW_SEASON)
        posterImage.setTransitionName(R.string.transition_season_poster)
    }

    override fun loadDetailContent() {
        presenter?.setSeasonNumber(tvShowSeason?.seasonNumber!!)
        presenter?.loadDetailContent(tvShowId)
    }

    override fun getBackdropPath() = tvShowSeason?.posterPath.getOriginalImageUrl()

    override fun getPosterPath() = tvShowSeason?.posterPath.getPosterUrl()

    override fun showDetailContent(detailContent: SeasonDetail) {
        detailContent.apply {
            titleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }
        super.showDetailContent(detailContent)
    }

    override fun getItemTitle(): String {
        return String.format(getString(R.string.season_number_format), tvShowSeason?.seasonNumber)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_SEASON

    override fun showEpisodeList(episodeList: List<Episode>) {
        episodesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_EPISODE,
                onEpisodeItemClickLitener)

        inflateViewStubRecyclerView(episodesViewStub, R.id.episodes_recycler_view, episodesAdapter!!, episodeList)
    }

    override fun performCleanup() {
        episodesAdapter?.removeListener()
        super.performCleanup()
    }
}