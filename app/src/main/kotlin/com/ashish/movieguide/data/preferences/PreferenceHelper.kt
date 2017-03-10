package com.ashish.movieguide.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 28.
 */
@Singleton
class PreferenceHelper @Inject constructor(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val PREF_ID = "id"
        private const val PREF_NAME = "name"
        private const val PREF_USER_NAME = "user_name"
        private const val PREF_SESSION_ID = "session_id"
        private const val PREF_GRAVATAR_HASH = "gravatar_hash"

        private const val PREF_ACCESS_TOKEN = "access_token"
    }

    fun getId(): Long = sharedPrefs.getLong(PREF_ID, 0L)

    fun setId(id: Long) = sharedPrefs.edit().putLong(PREF_ID, id).apply()

    fun getName(): String? = getString(PREF_NAME)

    fun setName(name: String?) = putString(PREF_NAME, name)

    fun getUserName(): String? = getString(PREF_USER_NAME)

    fun setUserName(userName: String?) = putString(PREF_USER_NAME, userName)

    fun getGravatarHash(): String? = getString(PREF_GRAVATAR_HASH)

    fun setGravatarHash(hash: String?) = putString(PREF_GRAVATAR_HASH, hash)

    fun getSessionId(): String? = getString(PREF_SESSION_ID)

    fun setSessionId(sessionId: String?) = putString(PREF_SESSION_ID, sessionId)

    fun getAccessToken(): String? = getString(PREF_ACCESS_TOKEN)

    fun setAccessToken(accessToken: String?) = putString(PREF_ACCESS_TOKEN, accessToken)

    private fun getString(key: String, defaultValue: String = ""): String? = sharedPrefs.getString(key, defaultValue)

    private fun putString(key: String, value: String?) = sharedPrefs.edit().putString(key, value).apply()

    fun clearUserData() {
        sharedPrefs.all.entries
                .map { it.key }
                .forEach { sharedPrefs.edit().remove(it).apply() }
    }
}