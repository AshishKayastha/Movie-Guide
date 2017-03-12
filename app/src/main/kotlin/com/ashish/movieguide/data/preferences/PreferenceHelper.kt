package com.ashish.movieguide.data.preferences

import android.content.SharedPreferences
import com.ashish.movieguide.data.models.trakt.ImageSizes
import com.ashish.movieguide.data.models.trakt.Images
import com.ashish.movieguide.data.models.trakt.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 28.
 */
@Singleton
class PreferenceHelper @Inject constructor(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val PREF_NAME = "name"
        private const val PREF_USER_NAME = "user_name"
        private const val PREF_ABOUT_USER = "about"
        private const val PREF_USER_AGE = "age"
        private const val PREF_USER_GENDER = "gender"
        private const val PREF_USER_IMAGE_URL = "image_url"
        private const val PREF_COVER_IMAGE_URL = "cover_image_url"
        private const val PREF_USER_LOCATION = "location"
        private const val PREF_USER_JOINED_AT = "joined_at"
        private const val PREF_IS_VIP = "is_vip"
        private const val PREF_IS_VIP_EP = "is_vip_ep"
        private const val PREF_IS_PRIVATE = "is_private"

        private const val PREF_ID = "id"
        private const val PREF_SESSION_ID = "session_id"
        private const val PREF_GRAVATAR_HASH = "gravatar_hash"

        private const val PREF_ACCESS_TOKEN = "access_token"
        private const val PREF_REFRESH_TOKEN = "refresh_token"
    }

    fun getId(): Long = sharedPrefs.getLong(PREF_ID, 0L)

    fun setId(id: Long) = sharedPrefs.edit().putLong(PREF_ID, id).apply()

    fun getName(): String? = getString(PREF_NAME)

    fun setName(name: String?) = putString(PREF_NAME, name)

    fun getUserName(): String? = getString(PREF_USER_NAME)

    fun setUserName(userName: String?) = putString(PREF_USER_NAME, userName)

    fun getImageUrl(): String? = getString(PREF_USER_IMAGE_URL)

    fun setImageUrl(imageUrl: String?) = putString(PREF_USER_IMAGE_URL, imageUrl)

    fun getCoverImageUrl(): String? = getString(PREF_COVER_IMAGE_URL)

    fun setCoverImageUrl(coverImageUrl: String?) = putString(PREF_COVER_IMAGE_URL, coverImageUrl)

    fun getAge(): Int = getInt(PREF_USER_AGE)

    fun setAge(age: Int) = putInt(PREF_USER_AGE, age)

    fun getLocation(): String? = getString(PREF_USER_LOCATION)

    fun setLocation(location: String?) = putString(PREF_USER_LOCATION, location)

    fun getGender(): String? = getString(PREF_USER_GENDER)

    fun setGender(gender: String?) = putString(PREF_USER_GENDER, gender)

    fun getAboutUser(): String? = getString(PREF_ABOUT_USER)

    fun setAboutUser(about: String?) = putString(PREF_ABOUT_USER, about)

    fun getJoinedAt(): String? = getString(PREF_USER_JOINED_AT)

    fun setJoinedAt(joinedAt: String?) = putString(PREF_USER_JOINED_AT, joinedAt)

    fun isPrivate(): Boolean = getBoolean(PREF_IS_PRIVATE)

    fun setIsPrivate(isPrivate: Boolean) = putBoolean(PREF_USER_JOINED_AT, isPrivate)

    fun isVIP(): Boolean = getBoolean(PREF_IS_VIP)

    fun setIsVIP(isVIP: Boolean) = putBoolean(PREF_IS_VIP, isVIP)

    fun isVIPEP(): Boolean = getBoolean(PREF_IS_VIP_EP)

    fun setIsVIPEP(isVIPEP: Boolean) = putBoolean(PREF_IS_VIP_EP, isVIPEP)

    fun getGravatarHash(): String? = getString(PREF_GRAVATAR_HASH)

    fun setGravatarHash(hash: String?) = putString(PREF_GRAVATAR_HASH, hash)

    fun getSessionId(): String? = getString(PREF_SESSION_ID)

    fun setSessionId(sessionId: String?) = putString(PREF_SESSION_ID, sessionId)

    fun getAccessToken(): String? = getString(PREF_ACCESS_TOKEN)

    fun setAccessToken(accessToken: String?) = putString(PREF_ACCESS_TOKEN, accessToken)

    fun getRefreshToken(): String? = getString(PREF_REFRESH_TOKEN)

    fun setRefreshToken(refreshToken: String?) = putString(PREF_REFRESH_TOKEN, refreshToken)

    private fun getString(key: String, defaultValue: String = ""): String? = sharedPrefs.getString(key, defaultValue)

    private fun putString(key: String, value: String?) = sharedPrefs.edit().putString(key, value).apply()

    private fun getInt(key: String, defaultValue: Int = 0): Int = sharedPrefs.getInt(key, defaultValue)

    private fun putInt(key: String, value: Int) = sharedPrefs.edit().putInt(key, value).apply()

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
            = sharedPrefs.getBoolean(key, defaultValue)

    private fun putBoolean(key: String, value: Boolean) = sharedPrefs.edit().putBoolean(key, value).apply()

    fun saveUserProfile(userProfile: UserProfile?) {
        userProfile?.apply {
            setName(name)
            setUserName(username)
            setImageUrl(images?.avatar?.full)
            setLocation(location)
            setAge(age)
            setAboutUser(about)
            setJoinedAt(joinedAt)
            setGender(gender)
            setIsPrivate(private)
            setIsVIP(vip)
            setIsVIPEP(vipEp)
        }
    }

    fun getUserProfile(): UserProfile {
        val avatar = ImageSizes(getImageUrl())
        return UserProfile(
                name = getName(),
                username = getUserName(),
                age = getAge(),
                gender = getGender(),
                location = getLocation(),
                about = getAboutUser(),
                images = Images(avatar),
                private = isPrivate(),
                vip = isVIP(),
                joinedAt = getJoinedAt(),
                vipEp = isVIPEP()
        )
    }

    fun clearUserData() {
        sharedPrefs.all.entries
                .map { it.key }
                .forEach { sharedPrefs.edit().remove(it).apply() }
    }
}