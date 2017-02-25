package com.ashish.movies.ui.tvshow.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.Season
import com.ashish.movies.data.models.SeasonDetail
import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movies.di.scopes.ActivityScope
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movies.ui.tvshow.episode.EpisodeDetailActivity
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.getPosterUrl
import com.ashish.movies.utils.extensions.setTitleAndYear
import icepick.State

/**
 * Created by Ashish on Jan 07.
 */
@ActivityScope
class SeasonDetailActivity : FullDetailContentActivity<SeasonDetail, SeasonDetailView, SeasonDetailPresenter>(),
        SeasonDetailView {

    companion object {
        private const val EXTRA_SEASON = "season"
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"

        fun createIntent(context: Context, tvShowId: Long?, season: Season?): Intent {
            return Intent(context, SeasonDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_SEASON, season)
        }
    }

    @JvmField @State var tvShowId: Long? = null
    @JvmField @State var season: Season? = null

    private val episodesViewStub: ViewStub by bindView(R.id.episodes_view_stub)

    private var episodesAdapter: RecyclerViewAdapter<Episode>? = null

    private val onEpisodeItemClickLitener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            val episode = episodesAdapter?.getItem<Episode>(position)
            val intent = EpisodeDetailActivity.createIntent(this@SeasonDetailActivity, tvShowId, episode)
            startNewActivityWithTransition(view, R.string.transition_episode_image, intent)
        }
    }

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(SeasonDetailActivity::class.java,
                SeasonDetailComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_season

    override fun getIntentExtras(extras: Bundle?) {
        tvShowId = extras?.getLong(EXTRA_TV_SHOW_ID)
        season = extras?.getParcelable(EXTRA_SEASON)
    }

    override fun getTransitionNameId() = R.string.transition_season_poster

    override fun loadDetailContent() {
        presenter?.setSeasonNumber(season?.seasonNumber!!)
        presenter?.loadDetailContent(tvShowId)
    }

    override fun getBackdropPath() = season?.posterPath.getOriginalImageUrl()

    override fun getPosterPath() = season?.posterPath.getPosterUrl()

    override fun showDetailContent(detailContent: SeasonDetail) {
        detailContent.apply {
            titleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }
        super.showDetailContent(detailContent)
    }

    override fun getItemTitle(): String {
        return String.format(getString(R.string.season_number_format), season?.seasonNumber)
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