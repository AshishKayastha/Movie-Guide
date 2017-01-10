package com.ashish.movies.ui.tvshow.episode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.ashish.movies.R
import com.ashish.movies.data.models.Episode
import com.ashish.movies.data.models.EpisodeDetail
import com.ashish.movies.data.models.OMDbDetail
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.ui.base.detail.BaseDetailActivity
import com.ashish.movies.ui.base.detail.BaseDetailMvpView
import com.ashish.movies.ui.common.adapter.OnItemClickListener
import com.ashish.movies.ui.widget.FontTextView
import com.ashish.movies.utils.extensions.applyText
import com.ashish.movies.utils.extensions.getFormattedReleaseDate
import com.ashish.movies.utils.extensions.getOriginalImageUrl
import com.ashish.movies.utils.extensions.setFlixterScore
import com.ashish.movies.utils.extensions.setIMDbRating
import com.ashish.movies.utils.extensions.setMetaScore
import com.ashish.movies.utils.extensions.setTMDbRating
import com.ashish.movies.utils.extensions.setTitleAndYear
import com.ashish.movies.utils.extensions.setTomatoRating
import com.ashish.movies.utils.extensions.setTransitionName

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailActivity : BaseDetailActivity<EpisodeDetail, BaseDetailMvpView<EpisodeDetail>, EpisodeDetailPresenter>() {

    private val airDateText: FontTextView by bindView(R.id.air_date_text)
    private val seasonNumberText: FontTextView by bindView(R.id.season_num_text)
    private val episodeNumberText: FontTextView by bindView(R.id.episode_num_text)

    private val metascoreView: View by bindView(R.id.metascore_view)
    private val imdbRatingView: View by bindView(R.id.imdb_rating_view)
    private val tmdbRatingView: View by bindView(R.id.tmdb_rating_view)
    private val flixterScoreView: View by bindView(R.id.flixter_score_view)
    private val tomatoRatingView: View by bindView(R.id.tomato_rating_view)

    private val flixterScoreImage: ImageView by bindView(R.id.flixter_score_image)
    private val tomatoRatingImage: ImageView by bindView(R.id.tomato_rating_image)

    private val metascoreText: FontTextView by bindView(R.id.metascore_text)
    private val imdbRatingText: FontTextView by bindView(R.id.imdb_rating_text)
    private val tmdbRatingText: FontTextView by bindView(R.id.tmdb_rating_text)
    private val flixterScoreText: FontTextView by bindView(R.id.flixter_score_text)
    private val tomatoRatingText: FontTextView by bindView(R.id.tomato_rating_text)

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
            tmdbRatingText.setTMDbRating(detailContent.voteAverage, tmdbRatingView)
            airDateText.text = airDate.getFormattedReleaseDate(this@EpisodeDetailActivity)
        }
        super.showDetailContent(detailContent)
    }

    override fun showOMDbDetail(omDbDetail: OMDbDetail) {
        with(omDbDetail) {
            omDbDetail.setMetaScore(metascoreView, metascoreText)
            omDbDetail.setIMDbRating(imdbRatingView, imdbRatingText)
            omDbDetail.setTomatoRating(tomatoRatingView, tomatoRatingText, tomatoRatingImage)
            omDbDetail.setFlixterScore(flixterScoreView, flixterScoreText, flixterScoreImage)
        }
    }

    override fun getCastItemClickListener() = onCastItemClickListener

    override fun getCrewItemClickListener() = onCrewItemClickListener

    override fun getItemTitle() = episode?.name ?: ""
}