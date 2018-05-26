package com.ashish.movieguide.ui.profile

import com.ashish.movieguide.R
import com.ashish.movieguide.data.network.api.trakt.UserApi
import com.ashish.movieguide.data.network.entities.trakt.Stats
import com.ashish.movieguide.data.preferences.PreferenceHelper
import com.ashish.movieguide.ui.base.mvp.RxPresenter
import com.ashish.movieguide.utils.extensions.isNotNullOrEmpty
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
        private val userApi: UserApi,
        private val preferenceHelper: PreferenceHelper,
        schedulerProvider: BaseSchedulerProvider
) : RxPresenter<ProfileView>(schedulerProvider) {

    fun loadUserProfile() {
        val userProfile = preferenceHelper.getUserProfile()
        view?.showUserProfile(userProfile, preferenceHelper.getCoverImageUrl())
        loadUserStats()
    }

    private fun loadUserStats() {
        val slug = preferenceHelper.getSlug()
        if (slug.isNotNullOrEmpty()) {
            addDisposable(userApi.getUserStats(slug!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ showUserStats(it) }, { handleError(it) }))
        }
    }

    private fun showUserStats(stats: Stats) {
        view?.run {
            with(stats) {
                movies?.let { showMovieStats(it) }
                episodes?.let { showEpisodeStats(it) }
                network?.let { showNetworkStats(it) }
                ratings?.let { showRatings(it) }
            }
        }
    }

    private fun handleError(t: Throwable) {
        Timber.e(t)
        view?.run {
            if (t is IOException) {
                showMessage(R.string.error_no_internet)
            } else {
                showMessage(R.string.error_retrieving_stats)
            }
        }
    }
}