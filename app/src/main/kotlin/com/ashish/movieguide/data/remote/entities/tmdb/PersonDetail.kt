package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 04.
 */
@JsonClass(generateAdapter = true)
data class PersonDetail(
        val id: Long? = null,
        val name: String? = null,
        val imdbId: String? = null,
        val birthday: String? = null,
        val biography: String? = null,
        val homepage: String? = null,
        val deathday: String? = null,
        val images: ProfileImages? = null,
        @Json(name = "profile_path") val profilePath: String? = null,
        @Json(name = "place_of_birth") val placeOfBirth: String? = null,
        @Json(name = "also_known_as") val alsoKnownAs: List<String>? = null,
        @Json(name = "combined_credits") val credits: CreditResults? = null
)