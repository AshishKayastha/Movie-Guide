package com.ashish.movieguide.ui.profile

import com.ashish.movieguide.data.network.entities.trakt.EpisodeStats
import com.ashish.movieguide.data.network.entities.trakt.MovieStats
import com.ashish.movieguide.data.network.entities.trakt.NetworkStats
import com.ashish.movieguide.data.network.entities.trakt.RatingDistribution
import com.ashish.movieguide.data.network.entities.trakt.UserProfile
import com.ashish.movieguide.ui.base.mvp.ProgressMvpView

interface ProfileView : ProgressMvpView {

    fun showUserProfile(userProfile: UserProfile, coverImageUrl: String?)

    fun showMovieStats(movieStats: MovieStats)

    fun showEpisodeStats(episodeStats: EpisodeStats)

    fun showNetworkStats(networkStats: NetworkStats)

    fun showRatings(ratingDistribution: RatingDistribution)
}