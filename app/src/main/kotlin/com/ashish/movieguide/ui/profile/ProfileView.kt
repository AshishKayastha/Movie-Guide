package com.ashish.movieguide.ui.profile

import com.ashish.movieguide.data.models.trakt.EpisodeStats
import com.ashish.movieguide.data.models.trakt.MovieStats
import com.ashish.movieguide.data.models.trakt.NetworkStats
import com.ashish.movieguide.data.models.trakt.UserProfile
import com.ashish.movieguide.ui.base.mvp.ProgressView

interface ProfileView : ProgressView {

    fun showUserProfile(userProfile: UserProfile, coverImageUrl: String?)

    fun showMovieStats(movieStats: MovieStats)

    fun showEpisodeStats(episodeStats: EpisodeStats)

    fun showNetworkStats(networkStats: NetworkStats)
}