package com.ashish.movies.ui.tvshow.episode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movies.ui.base.detail.fulldetail.FullDetailContentView
import com.ashish.movies.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.setTitleAndYear
import icepick.State

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailActivity : FullDetailContentActivity<EpisodeDetail,
        FullDetailContentView<EpisodeDetail>, EpisodeDetailPresenter>() {

    companion object {
        private const val EXTRA_EPISODE = "episode"
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"

        fun createIntent(context: Context, tvShowId: Long?, episode: Episode?): Intent {
            return Intent(context, EpisodeDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_EPISODE, episode)
        }
    }

    @JvmField @State var tvShowId: Long? = null
    @JvmField @State var episode: Episode? = null

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(EpisodeDetailActivity::class.java,
                EpisodeDetailComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_episode

    override fun getIntentExtras(extras: Bundle?) {
        tvShowId = extras?.getLong(EXTRA_TV_SHOW_ID)
        episode = extras?.getParcelable(EXTRA_EPISODE)
    }

    override fun getTransitionNameId() = R.string.transition_episode_image

    override fun loadDetailContent() {
        presenter?.setSeasonAndEpisodeNumber(episode?.seasonNumber!!, episode?.episodeNumber!!)
        presenter?.loadDetailContent(tvShowId)
    }

    override fun getBackdropPath() = episode?.stillPath.getOriginalImageUrl()

    override fun getPosterPath() = getBackdropPath()

    override fun showDetailContent(detailContent: EpisodeDetail) {
        detailContent.apply {
            titleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_EPISODE

    override fun getItemTitle() = episode?.name ?: ""
}