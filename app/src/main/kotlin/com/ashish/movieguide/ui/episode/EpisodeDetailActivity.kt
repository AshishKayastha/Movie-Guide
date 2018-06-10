package com.ashish.movieguide.ui.episode

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.tmdb.Episode
import com.ashish.movieguide.data.network.entities.tmdb.EpisodeDetail
import com.ashish.movieguide.data.network.entities.trakt.TraktEpisode
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.rating.RatingDialog
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.TMDbConstants.TMDB_URL
import com.ashish.movieguide.utils.extensions.getOriginalImageUrl
import com.ashish.movieguide.utils.extensions.getStillImageUrl
import com.ashish.movieguide.utils.extensions.performAction
import com.ashish.movieguide.utils.extensions.setRatingItemTitle
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.show
import com.evernote.android.state.State
import dagger.Lazy
import kotlinx.android.synthetic.main.activity_detail_episode.*
import kotlinx.android.synthetic.main.layout_detail_app_bar.*
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailActivity : FullDetailContentActivity<EpisodeDetail, TraktEpisode,
        EpisodeDetailView, EpisodeDetailPresenter>(),
        EpisodeDetailView, RatingDialog.UpdateRatingListener {

    companion object {
        private const val EXTRA_EPISODE = "episode"
        private const val EXTRA_TV_SHOW_ID = "tv_show_id"

        fun createIntent(context: Context, tvShowId: Long?, episode: Episode?): Intent {
            return Intent(context, EpisodeDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_EPISODE, episode)
        }
    }

    @Inject lateinit var ratingDialog: Lazy<RatingDialog>
    @Inject lateinit var preferenceHelper: PreferenceHelper
    @Inject lateinit var episodeDetailPresenter: EpisodeDetailPresenter

    @State var tvShowId: Long? = null
    @State var episode: Episode? = null

    private val isLoggedIn: Boolean by lazy { preferenceHelper.getId() > 0 }

    override fun getLayoutId() = R.layout.activity_detail_episode

    override fun providePresenter(): EpisodeDetailPresenter = episodeDetailPresenter

    override fun getIntentExtras(extras: Bundle?) {
        tvShowId = extras?.getLong(EXTRA_TV_SHOW_ID)
        episode = extras?.getParcelable(EXTRA_EPISODE)
    }

    override fun getTransitionNameId() = R.string.transition_episode_image

    override fun loadDetailContent() {
        episodeDetailPresenter.setSeasonAndEpisodeNumber(episode?.seasonNumber!!, episode?.episodeNumber!!)
        episodeDetailPresenter.loadDetailContent(tvShowId)
    }

    override fun getBackdropPath() = episode?.stillPath.getOriginalImageUrl()

    override fun getPosterPath() = episode?.stillPath.getStillImageUrl()

    override fun showDetailContent(detailContent: EpisodeDetail) {
        detailContent.apply {
            contentTitleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }

        performActionIfLoggedIn {
            it.setRatingListener(this)
            menu?.setRatingItemTitle(R.string.title_rate_episode)
        }

        detailEpisodeContainer.show()
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_EPISODE

    override fun getItemTitle(): String = episode?.name ?: ""

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_rating -> performAction {
            performActionIfLoggedIn { it.showRatingDialog(myRatingLabel.getRating()) }
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun getShareText(): CharSequence {
        return "${TMDB_URL}tv/${tvShowId}season/${episode!!.seasonNumber}episode/${episode!!.episodeNumber}"
    }

    override fun showSavedRating(rating: Int?) {
        myRatingLabel.setRating(rating)
    }

    override fun addRating(rating: Int) {
        episodeDetailPresenter.addRating(rating)
    }

    override fun removeRating() {
        episodeDetailPresenter.removeRating()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        performActionIfLoggedIn { it.dismissDialog() }
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        performActionIfLoggedIn { it.dismissDialog() }
        super.onStop()
    }

    override fun performCleanup() {
        super.performCleanup()
        performActionIfLoggedIn { it.setRatingListener(null) }
    }

    private fun performActionIfLoggedIn(action: (RatingDialog) -> Unit) {
        if (isLoggedIn) action(ratingDialog.get())
    }
}