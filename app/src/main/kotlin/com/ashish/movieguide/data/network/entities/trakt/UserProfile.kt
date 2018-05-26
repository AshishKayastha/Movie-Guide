package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfile(
        val ids: UserIds? = null,
        val name: String? = null,
        val username: String? = null,
        val age: Int? = null,
        val gender: String? = null,
        val location: String? = null,
        val about: String? = null,
        val images: Images? = null,
        val private: Boolean? = null,
        val vip: Boolean? = null,
        @Json(name = "joined_at") var joinedAt: String? = null,
        @Json(name = "vip_ep") val vipEp: Boolean? = null // VIP Executive Producer
)

data class UserIds(val slug: String? = null)