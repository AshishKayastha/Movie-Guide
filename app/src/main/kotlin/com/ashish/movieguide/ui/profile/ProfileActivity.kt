package com.ashish.movieguide.ui.profile

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
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
import com.ashish.movieguide.data.models.trakt.UserProfile
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.ashish.movieguide.ui.base.mvp.MvpActivity
import com.ashish.movieguide.ui.widget.FontTextView
import com.ashish.movieguide.utils.CircularTransformation
import com.ashish.movieguide.utils.DialogUtils
import com.ashish.movieguide.utils.extensions.applyText
import com.ashish.movieguide.utils.extensions.bindView
import com.ashish.movieguide.utils.extensions.changeViewGroupTextFont
import com.ashish.movieguide.utils.extensions.get
import com.ashish.movieguide.utils.extensions.getColorCompat
import com.ashish.movieguide.utils.extensions.hide
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.extensions.loadPaletteBitmap
import com.ashish.movieguide.utils.extensions.setTopBarColorAndAnimate
import com.ashish.movieguide.utils.extensions.show
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import dagger.Lazy
import javax.inject.Inject

class ProfileActivity : MvpActivity<ProfileView, ProfilePresenter>(), ProfileView {

    @Inject lateinit var dialogUtils: Lazy<DialogUtils>
    @Inject lateinit var preferenceHelper: PreferenceHelper

    private val coverImage: ImageView by bindView(R.id.cover_bg)
    private val userImage: ImageView by bindView(R.id.user_image)
    private val progressBar: ProgressBar by bindView(R.id.progress_bar)
    private val userNameText: FontTextView by bindView(R.id.user_name_text)
    private val friendsCountText: FontTextView by bindView(R.id.friend_count_text)
    private val followersCountText: FontTextView by bindView(R.id.follower_count_text)
    private val followingCountText: FontTextView by bindView(R.id.following_count_text)
    private val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()

        toolbar?.changeViewGroupTextFont()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun showUserProfile(userProfile: UserProfile, coverImageUrl: String?) {
        loadCoverImage(coverImageUrl)
        userNameText.applyText(userProfile.username)
        loadProfileImage(userProfile.images?.avatar?.full)
    }

    private fun loadCoverImage(coverImageUrl: String?) {
        coverImage.loadPaletteBitmap(coverImageUrl) { paletteBitmap ->
            setTopBarColorAndAnimate(paletteBitmap, collapsingToolbar) {
                val primaryBlack = getColorCompat(R.color.primary_text_dark)

                val backButton = toolbar[0] as ImageButton?
                backButton?.setColorFilter(primaryBlack)
                collapsingToolbar.setCollapsedTitleTextColor(primaryBlack)
            }
        }
    }

    private fun loadProfileImage(imageUrl: String?) {
        if (imageUrl.isNotNullOrEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .asBitmap()
                    .transform(CircularTransformation(Glide.get(this).bitmapPool))
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(bitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            bitmap?.let { userImage.setImageBitmap(it) }
                            startPostponedEnterTransition()
                        }
                    })
        } else {
            startPostponedEnterTransition()
        }
    }

    override fun showMovieStats(movieStats: MovieStats) {

    }

    override fun showEpisodeStats(episodeStats: EpisodeStats) {

    }

    override fun showNetworkStats(networkStats: NetworkStats) {
        with(networkStats) {
            friendsCountText.applyText(getCountString(R.plurals.friends, friends ?: 0))
            followersCountText.applyText(getCountString(R.plurals.followers, followers ?: 0))
            followingCountText.applyText(getCountString(R.plurals.followings, following ?: 0))
        }
    }

    private fun getCountString(pluralId: Int, count: Int): String {
        return resources.getQuantityString(pluralId, count, count)
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_log_out -> performAction { showLogOutDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLogOutDialog() {
        dialogUtils.get().buildDialog()
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
        dialogUtils.get().dismissAllDialogs()
        super.onConfigurationChanged(newConfig)
    }

    override fun onStop() {
        dialogUtils.get().dismissAllDialogs()
        super.onStop()
    }
}