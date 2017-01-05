package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Jan 04.
 */
data class PersonDetail(
        val id: Int? = null,
        val name: String? = null,
        val imdbId: String? = null,
        val birthday: String? = null,
        val biography: String? = null,
        val popularity: Double? = null,
        val homepage: String? = null,
        val deathday: String? = null,
        @Json(name = "profile_path") val profilePath: String? = null,
        @Json(name = "place_of_birth") val placeOfBirth: String? = null,
        @Json(name = "also_known_as") val alsoKnownAs: List<String>? = null,
        @Json(name = "combined_credits") val combinedCredits: CreditResults? = null
)