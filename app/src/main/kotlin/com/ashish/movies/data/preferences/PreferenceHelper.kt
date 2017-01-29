package com.ashish.movies.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ashish.movies.di.annotations.ForApplication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 28.
 */
@Singleton
class PreferenceHelper @Inject constructor(@ForApplication context: Context) {

    companion object {
        private const val PREF_NAME = "name"
        private const val PREF_USER_NAME = "user_name"
        private const val PREF_SESSION_ID = "session_id"
        private const val PREF_GRAVATAR_HASH = "gravatar_hash"
    }

    private val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun getName(): String = getString(PREF_NAME)

    fun setName(name: String?) = putString(PREF_NAME, name)

    fun getUserName(): String = getString(PREF_USER_NAME)

    fun setUserName(userName: String?) = putString(PREF_USER_NAME, userName)

    fun getGravatarHash(): String = getString(PREF_GRAVATAR_HASH)

    fun setGravatarHash(hash: String?) = putString(PREF_GRAVATAR_HASH, hash)

    fun getSessionId(): String = getString(PREF_SESSION_ID)

    fun setSessionId(sessionId: String?) = putString(PREF_SESSION_ID, sessionId)

    private fun getString(key: String, defaultValue: String = ""): String = sharedPrefs.getString(key, defaultValue)

    private fun putString(key: String, value: String?) = sharedPrefs.edit().putString(key, value).apply()
}