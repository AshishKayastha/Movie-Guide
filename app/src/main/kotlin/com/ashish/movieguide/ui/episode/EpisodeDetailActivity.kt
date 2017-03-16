package com.ashish.movieguide.ui.episode

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.tmdb.Episode
import com.ashish.movieguide.data.models.tmdb.EpisodeDetail
import com.ashish.movieguide.data.models.trakt.TraktEpisode
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.rating.RatingDialog
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.TMDbConstants.TMDB_URL
import com.ashish.movieguide.utils.extensions.getOriginalImageUrl
import com.ashish.movieguide.utils.extensions.getStillImageUrl
import com.ashish.movieguide.utils.extensions.setRatingItemTitle
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import com.ashish.movieguide.utils.extensions.show
import dagger.Lazy
import icepick.State
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

        @JvmStatic
        fun createIntent(context: Context, tvShowId: Long?, episode: Episode?): Intent {
            return Intent(context, EpisodeDetailActivity::class.java)
                    .putExtra(EXTRA_TV_SHOW_ID, tvShowId)
                    .putExtra(EXTRA_EPISODE, episode)
        }
    }

    @Inject lateinit var ratingDialog: Lazy<RatingDialog>
    @Inject lateinit var preferenceHelper: PreferenceHelper

    @JvmField @State var tvShowId: Long? = null
    @JvmField @State var episode: Episode? = null

    private val isLoggedIn: Boolean by lazy {
        preferenceHelper.getId() > 0
    }

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

    override fun getPosterPath() = episode?.stillPath.getStillImageUrl()

    override fun showDetailContent(detailContent: EpisodeDetail) {
        detailContent.run {
            contentTitleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }

        if (isLoggedIn) {
            ratingDialog.get().setRatingListener(this)
            menu?.setRatingItemTitle(R.string.title_rate_episode)
        }

        detailEpisodeContainer.show()
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_EPISODE

    override fun getItemTitle() = episode?.name ?: ""

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_rating -> performAction {
            if (isLoggedIn) {
                ratingDialog.get().showRatingDialog(myRatingLabel.getRating())
            }
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun getShareText(): CharSequence {
        return TMDB_URL + "tv/" + tvShowId + "season/" + episode!!.seasonNumber +
                "episode/" + episode!!.episodeNumber
    }

    override fun showSavedRating(rating: Int?) {
        myRatingLabel.setRating(rating)
    }

    override fun addRating(rating: Int) {
        presenter?.addRating(rating)
    }

    override fun removeRating() {
        presenter?.removeRating()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        if (isLoggedIn) ratingDialog.get().dismissDialog()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        if (isLoggedIn) ratingDialog.get().dismissDialog()
        super.onStop()
    }

    override fun performCleanup() {
        super.performCleanup()
        if (isLoggedIn) ratingDialog.get().setRatingListener(null)
    }
}