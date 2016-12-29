package com.ashish.movies.data.models

data class TVShowResults(
        val page: Int = 1,
        val totalPages: Int = 0,
        val results: List<TVShow?>? = null,
        val totalResults: Int = 0
)