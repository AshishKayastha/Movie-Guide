package com.ashish.movieguide.data.models.trakt

import com.squareup.moshi.Json

data class UserProfile(
        val name: String? = null,
        val username: String? = null,
        val age: Int = 0,
        val gender: String? = null,
        val location: String? = null,
        val about: String? = null,
        val images: Images? = null,
        val private: Boolean = false,
        val vip: Boolean = false,
        @Json(name = "joined_at") var joinedAt: String? = null,
        @Json(name = "vip_ep") val vipEp: Boolean = false // VIP Executive Producer
)