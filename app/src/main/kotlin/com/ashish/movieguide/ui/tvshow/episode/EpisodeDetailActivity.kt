package com.ashish.movieguide.ui.tvshow.episode

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.data.models.EpisodeDetail
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.detail.fulldetail.FullDetailContentActivity
import com.ashish.movieguide.ui.common.rating.RatingDialog
import com.ashish.movieguide.utils.Constants.ADAPTER_TYPE_EPISODE
import com.ashish.movieguide.utils.extensions.getOriginalImageUrl
import com.ashish.movieguide.utils.extensions.getStillImageUrl
import com.ashish.movieguide.utils.extensions.setRatingItemTitle
import com.ashish.movieguide.utils.extensions.setTitleAndYear
import icepick.State
import javax.inject.Inject

/**
 * Created by Ashish on Jan 08.
 */
class EpisodeDetailActivity : FullDetailContentActivity<EpisodeDetail, EpisodeDetailView,
        EpisodeDetailPresenter>(), EpisodeDetailView, RatingDialog.UpdateRatingListener {

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

    @Inject lateinit var ratingDialog: RatingDialog

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

    override fun getPosterPath() = episode?.stillPath.getStillImageUrl()

    override fun showDetailContent(detailContent: EpisodeDetail) {
        detailContent.apply {
            titleText.setTitleAndYear(name, airDate)
            imdbId = detailContent.externalIds?.imdbId
        }

        ratingDialog.setRatingListener(this)
        menu?.setRatingItemTitle(R.string.title_rate_episode)
        super.showDetailContent(detailContent)
    }

    override fun getDetailContentType() = ADAPTER_TYPE_EPISODE

    override fun getItemTitle() = episode?.name ?: ""

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_rating -> performAction {
            ratingDialog.showRatingDialog(ratingLabelLayout.getRating())
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun showSavedRating(rating: Double?) {
        ratingLabelLayout.setRating(rating)
    }

    override fun saveRating(rating: Double) {
        presenter?.saveRating(rating)
    }

    override fun deleteRating() {
        presenter?.deleteRating()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        ratingDialog.dismissDialog()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        ratingDialog.dismissDialog()
        super.onStop()
    }

    override fun performCleanup() {
        ratingDialog.setRatingListener(null)
        super.performCleanup()
    }
}