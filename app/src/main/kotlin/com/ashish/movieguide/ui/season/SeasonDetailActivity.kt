package com.ashish.movieguide.ui.season

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.data.models.Season
import com.ashish.movieguide.data.models.SeasonDetail
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.adapter.OnItemClickListener
import com.ashish.movieguide.ui.common.adapter.RecyclerViewAdapter
import com.ashish.movieguide.ui.episode.EpisodeDetailActivity
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_SEASON
import com.ashish.movieguide.utils.Constants.TMDB_URL
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.getOriginalImageUrl
import com.ashish.movieguide.utils.extensions.getPosterUrl
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import icepick.State

/**
 * Created by Ashish on Jan 07.
 */
class SeasonDetailActivity : FullDetailContentActivity<SeasonDetail, SeasonDetailView, SeasonDetailPresenter>(),
        SeasonDetailView {

    companion object {
        private const val EXTRA_SEASON = "season"
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"

        @JvmStatic
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
        val seasonNumber = season?.seasonNumber ?: 0
        return if (seasonNumber > 0) {
            String.format(getString(R.string.season_number_format), seasonNumber)
        } else {
            return getString(R.string.season_specials)
        }
    }

    override fun getDetailContentType() = ADAPTER_TYPE_SEASON

    override fun showEpisodeList(episodeList: List<Episode>) {
        episodesAdapter = RecyclerViewAdapter(R.layout.list_item_content_alt, ADAPTER_TYPE_EPISODE,
                onEpisodeItemClickLitener)

        inflateViewStubRecyclerView(episodesViewStub, R.id.episodes_recycler_view, episodesAdapter!!, episodeList)
    }

    override fun getShareText(): CharSequence {
        return TMDB_URL + "tv/" + tvShowId + "season/" + season!!.seasonNumber
    }

    override fun performCleanup() {
        episodesAdapter?.removeListener()
        super.performCleanup()
    }
}