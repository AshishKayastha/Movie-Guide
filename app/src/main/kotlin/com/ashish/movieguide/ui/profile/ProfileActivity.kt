package com.ashish.movieguide.ui.profile

import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.PluralsRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import com.ashish.movieguide.R
import com.ashish.movieguide.data.models.trakt.EpisodeStats
import com.ashish.movieguide.data.models.trakt.MovieStats
import com.ashish.movieguide.data.models.trakt.NetworkStats
import com.ashish.movieguide.data.models.trakt.Ratings
import com.ashish.movieguide.data.models.trakt.UserProfile
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.ui.widget.RatingCountLayout
import com.ashish.movieguide.ui.widget.TimeSpentView
import com.ashish.movieguide.utils.DialogUtils
import com.ashish.movieguide.utils.StartTransitionListener
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeTitleTypeface
import com.ashish.movieguide.utils.extensions.get
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.getDayHourMinutes
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.loadCircularImage
import com.ashish.movieguide.utils.extensions.loadPaletteBitmap
import com.ashish.movieguide.utils.extensions.setTopBarColorAndAnimate
import com.ashish.movieguide.utils.extensions.show
import com.ashish.movieguide.utils.extensions.tint
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import javax.inject.Inject

class ProfileActivity : MvpActivity<ProfileView, ProfilePresenter>(), ProfileView,
        AppBarLayout.OnOffsetChangedListener {

    @Inject lateinit var dialogUtils: DialogUtils
    @Inject lateinit var preferenceHelper: PreferenceHelper

    private val appBar: AppBarLayout by bindView(R.id.app_bar)
    private val coverImage: ImageView by bindView(R.id.cover_bg)
    private val userImage: ImageView by bindView(R.id.user_image)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val displayNameText: FontTextView by bindView(R.id.display_name_text)
    private val friendsCountText: FontTextView by bindView(R.id.friend_count_text)
    private val followersCountText: FontTextView by bindView(R.id.follower_count_text)
    private val followingCountText: FontTextView by bindView(R.id.following_count_text)
    private val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    private val watchedMoviesText: FontTextView by bindView(R.id.watched_movies_text)
    private val moviesDaySpentView: TimeSpentView by bindView(R.id.movies_days_spent_view)
    private val moviesHourSpentView: TimeSpentView by bindView(R.id.movies_hours_spent_view)
    private val moviesMinuteSpentView: TimeSpentView by bindView(R.id.movies_minutes_spent_view)

    private val watchedEpisodesText: FontTextView by bindView(R.id.watched_episodes_text)
    private val episodesDaySpentView: TimeSpentView by bindView(R.id.episodes_days_spent_view)
    private val episodesHourSpentView: TimeSpentView by bindView(R.id.episodes_hours_spent_view)
    private val episodesMinuteSpentView: TimeSpentView by bindView(R.id.episodes_minutes_spent_view)

    private val tenStarView: RatingCountLayout by bindView(R.id.ten_star_view)
    private val nineStarView: RatingCountLayout by bindView(R.id.nine_star_view)
    private val eightStarView: RatingCountLayout by bindView(R.id.eight_star_view)
    private val sevenStarView: RatingCountLayout by bindView(R.id.seven_star_view)
    private val sixStarView: RatingCountLayout by bindView(R.id.six_star_view)
    private val fiveStarView: RatingCountLayout by bindView(R.id.five_star_view)
    private val fourStarView: RatingCountLayout by bindView(R.id.four_star_view)
    private val threeStarView: RatingCountLayout by bindView(R.id.three_star_view)
    private val twoStarView: RatingCountLayout by bindView(R.id.two_star_view)
    private val oneStarView: RatingCountLayout by bindView(R.id.one_star_view)

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
        displayName = userProfile.name ?: ""
        displayNameText.applyText(displayName)

        loadCoverImage(coverImageUrl)
        loadProfileImage(userProfile.images?.avatar?.full)
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
            userImage.loadCircularImage(imageUrl, StartTransitionListener<GlideDrawable>(this))
        } else {
            startPostponedEnterTransition()
        }
    }

    override fun showMovieStats(movieStats: MovieStats) {
        val watchedMoviesCount = movieStats.watched ?: 0
        watchedMoviesText.text = watchedMoviesCount.toString()

        val (day, hour, minutes) = movieStats.minutes?.getDayHourMinutes()!!
        moviesDaySpentView.setTimeSpentCount(day)
        moviesHourSpentView.setTimeSpentCount(hour)
        moviesMinuteSpentView.setTimeSpentCount(minutes)
    }

    override fun showEpisodeStats(episodeStats: EpisodeStats) {
        val watchedEpisodesCount = episodeStats.watched ?: 0
        watchedEpisodesText.text = watchedEpisodesCount.toString()

        val (day, hour, minutes) = episodeStats.minutes?.getDayHourMinutes()!!
        episodesDaySpentView.setTimeSpentCount(day)
        episodesHourSpentView.setTimeSpentCount(hour)
        episodesMinuteSpentView.setTimeSpentCount(minutes)
    }

    override fun showNetworkStats(networkStats: NetworkStats) {
        with(networkStats) {
            friendsCountText.applyText(getCountString(R.plurals.friends, friends ?: 0))
            followersCountText.applyText(getCountString(R.plurals.followers, followers ?: 0))
            followingCountText.applyText(getCountString(R.plurals.followings, following ?: 0))
        }
    }

    private fun getCountString(@PluralsRes pluralId: Int, count: Int): String
            = resources.getQuantityString(pluralId, count, count)

    override fun showRatings(ratings: Ratings) {
        totalRatings = ratings.total ?: 0
        if (totalRatings > 0) {
            ratings.distribution?.apply {
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
            view.apply {
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
                    finish()
                })
                .show()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        dialogUtils.dismissAllDialogs()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        dialogUtils.dismissAllDialogs()
        super.onStop()
    }

    override fun onDestroy() {
        appBar.removeOnOffsetChangedListener(this)
        super.onDestroy()
    }
}