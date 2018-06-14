package com.ashish.movieguide.data.remote.entities.trakt

data class TraktEpisode(
        val number: Int? = null,
        val season: Int? = null,
        val ids: EpisodeIds? = null,
        val title: String? = null
)