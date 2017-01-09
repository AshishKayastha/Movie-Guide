package com.ashish.movies.ui.tvshow.episode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.setTitleAndYear
import com.ashish.movies.utils.extensions.setTransitionName

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailActivity : BaseDetailActivity<EpisodeDetail, BaseDetailMvpView<EpisodeDetail>, EpisodeDetailPresenter>() {

    private val airDateText: FontTextView by bindView(R.id.air_date_text)
    private val seasonNumberText: FontTextView by bindView(R.id.season_num_text)
    private val episodeNumberText: FontTextView by bindView(R.id.episode_num_text)

    private var tvShowId: Long? = null
    private var episode: Episode? = null

    private val onCastItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            startPersonDetailActivity(castAdapter, position, view)
        }
    }

    private val onCrewItemClickListener = object : OnItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            startPersonDetailActivity(crewAdapter, position, view)
        }
    }

    companion object {
        private const val EXTRA_EPISODE = "episode"
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"

        fun createIntent(context: Context, tvShowId: Long?, episode: Episode?): Intent {
            return Intent(context, EpisodeDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_EPISODE, episode)
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(EpisodeDetailModule()).inject(this)
    }

    override fun getLayoutId() = R.layout.activity_detail_episode

    override fun getIntentExtras(extras: Bundle?) {
        tvShowId = extras?.getLong(EXTRA_TV_SHOW_ID)
        episode = extras?.getParcelable(EXTRA_EPISODE)
        posterImage.setTransitionName(R.string.transition_episode_image)
    }

    override fun loadDetailContent() {
        presenter.setSeasonAndEpisodeNumber(episode?.seasonNumber!!, episode?.episodeNumber!!)
        presenter.loadDetailContent(tvShowId)
    }

    override fun getBackdropPath() = episode?.stillPath.getOriginalImageUrl()

    override fun getPosterPath() = getBackdropPath()

    override fun showDetailContent(detailContent: EpisodeDetail) {
        detailContent.apply {
            overviewText.applyText(overview)
            titleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
            seasonNumberText.text = seasonNumber.toString()
            episodeNumberText.text = episodeNumber.toString()
            airDateText.text = airDate.getFormattedReleaseDate(this@EpisodeDetailActivity)
        }
        super.showDetailContent(detailContent)
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener

    override fun getItemTitle() = episode?.name ?: ""
}