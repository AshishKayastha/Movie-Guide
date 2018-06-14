package com.ashish.movieguide.data.remote.entities.trakt

data class SyncItems(
        val movies: List<SyncMovie?>? = null,
        val shows: List<SyncShow?>? = null,
        val episodes: List<SyncEpisode?>? = null
)