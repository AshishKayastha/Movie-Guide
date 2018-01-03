package com.ashish.movieguide.ui.profile

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.PluralsRes
import android.support.design.widget.AppBarLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.entities.trakt.EpisodeStats
import com.ashish.movieguide.data.network.entities.trakt.MovieStats
import com.ashish.movieguide.data.network.entities.trakt.NetworkStats
import com.ashish.movieguide.data.network.entities.trakt.RatingDistribution
import com.ashish.movieguide.data.network.entities.trakt.UserProfile
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.widget.RatingCountLayout
import com.ashish.movieguide.utils.Constants.GMT_ISO8601_FORMAT
import com.ashish.movieguide.utils.DialogUtils
import com.ashish.movieguide.utils.StartTransitionListener
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.changeTitleTypeface
import com.ashish.movieguide.utils.extensions.get
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.getDayHourMinutes
import com.ashish.movieguide.utils.extensions.getFormattedMediumDate
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.loadCircularImage
import com.ashish.movieguide.utils.extensions.loadPaletteBitmap
import com.ashish.movieguide.utils.extensions.setTopBarColorAndAnimate
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.tint
import kotlinx.android.synthetic.main.layout_profile_app_bar.*
import kotlinx.android.synthetic.main.layout_profile_episode_stats.*
import kotlinx.android.synthetic.main.layout_profile_movie_stats.*
import kotlinx.android.synthetic.main.layout_profile_ratings.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject

class ProfileActivity : MvpActivity<ProfileView, ProfilePresenter>(), ProfileView,
        AppBarLayout.OnOffsetChangedListener {

    @Inject lateinit var dialogUtils: DialogUtils
    @Inject lateinit var preferenceHelper: PreferenceHelper

    private var menu: Menu? = null
    private var totalRatings: Int = 0
    private var displayName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()

        collapsingToolbar.changeTitleTypeface()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBar.addOnOffsetChangedListener(this)
    }

    override fun getLayoutId() = R.layout.activity_profile

    override fun injectDependencies(builderHost: ActivityComponentBuilderHost) {
        builderHost.getActivityComponentBuilder(ProfileActivity::class.java, ProfileComponent.Builder::class.java)
                .withModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter?.loadUserProfile()
    }

    override fun showProgress() = progressBar.show()

    override fun hideProgress() = progressBar.hide()

    override fun showUserProfile(userProfile: UserProfile, coverImageUrl: String?) {
        with(userProfile) {
            displayName = name ?: ""
            displayNameText.applyText(displayName)
            locationText.applyText(location)

            val formattedDate = joinedAt.getFormattedMediumDate(GMT_ISO8601_FORMAT)
            if (formattedDate.isNotNullOrEmpty()) {
                joinedOnText.text = String.format(getString(R.string.joined_on_format), formattedDate)
            }

            loadCoverImage(coverImageUrl)
            loadProfileImage(images?.avatar?.full)
        }
    }

    private fun loadCoverImage(coverImageUrl: String?) {
        coverImage.loadPaletteBitmap(coverImageUrl) { paletteBitmap ->
            setTopBarColorAndAnimate(paletteBitmap, collapsingToolbar) {
                val primaryBlack = getColorCompat(R.color.primary_text_dark)

                menu?.tint(primaryBlack)
                val backButton = toolbar[0] as ImageButton?
                backButton?.setColorFilter(primaryBlack)
                collapsingToolbar.setCollapsedTitleTextColor(primaryBlack)
            }
        }
    }

    private fun loadProfileImage(imageUrl: String?) {
        if (imageUrl.isNotNullOrEmpty()) {
            userImage.loadCircularImage(imageUrl, StartTransitionListener(this))
        } else {
            startPostponedEnterTransition()
        }
    }

    override fun showMovieStats(movieStats: MovieStats) {
        val watchedMoviesCount = movieStats.watched ?: 0
        watchedMoviesText.text = watchedMoviesCount.toString()

        val (day, hour, minutes) = movieStats.minutes?.getDayHourMinutes()!!
        moviesDaysSpentView.setTimeSpentCount(day)
        moviesHoursSpentView.setTimeSpentCount(hour)
        moviesMinutesSpentView.setTimeSpentCount(minutes)
    }

    override fun showEpisodeStats(episodeStats: EpisodeStats) {
        val watchedEpisodesCount = episodeStats.watched ?: 0
        watchedEpisodesText.text = watchedEpisodesCount.toString()

        val (day, hour, minutes) = episodeStats.minutes?.getDayHourMinutes()!!
        episodesDaysSpentView.setTimeSpentCount(day)
        episodesHoursSpentView.setTimeSpentCount(hour)
        episodesMinutesSpentView.setTimeSpentCount(minutes)
    }

    override fun showNetworkStats(networkStats: NetworkStats) {
        with(networkStats) {
            friendCountText.applyText(getCountString(R.plurals.friends, friends ?: 0))
            followerCountText.applyText(getCountString(R.plurals.followers, followers ?: 0))
            followingCountText.applyText(getCountString(R.plurals.followings, following ?: 0))
        }
    }

    private fun getCountString(@PluralsRes pluralId: Int, count: Int): String
            = resources.getQuantityString(pluralId, count, count)

    override fun showRatings(ratingDistribution: RatingDistribution) {
        totalRatings = ratingDistribution.total ?: 0
        if (totalRatings > 0) {
            ratingDistribution.distribution?.run {
                setRatingStars(tenStarView, ten)
                setRatingStars(nineStarView, nine)
                setRatingStars(eightStarView, eight)
                setRatingStars(sevenStarView, seven)
                setRatingStars(sixStarView, six)
                setRatingStars(fiveStarView, five)
                setRatingStars(fourStarView, four)
                setRatingStars(threeStarView, three)
                setRatingStars(twoStarView, two)
                setRatingStars(oneStarView, one)
            }
        }
    }

    private fun setRatingStars(view: RatingCountLayout, ratingCount: Int?) {
        if (ratingCount != null) {
            view.run {
                setRatingCount(ratingCount.toString())
                setRatingProgress(totalRatings, ratingCount)
            }
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            collapsingToolbar.title = displayName
        } else {
            collapsingToolbar.title = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_log_out -> performAction { showLogOutDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLogOutDialog() {
        dialogUtils.buildDialog()
                .withTitle(R.string.title_log_out)
                .withContent(R.string.content_log_out)
                .withNegativeButton(android.R.string.cancel)
                .withPositiveButton(R.string.title_log_out, {
                    preferenceHelper.clearUserData()
                    setResult(Activity.RESULT_OK)
                    finish()
                })
                .show()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        dismissDialog()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        dismissDialog()
        super.onStop()
    }

    private fun dismissDialog() {
        if (isFinishing) dialogUtils.dismissAllDialogs()
    }

    override fun onDestroy() {
        appBar.removeOnOffsetChangedListener(this)
        super.onDestroy()
    }
}