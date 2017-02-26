package com.ashish.movieguide.utils

import android.util.Log

import timber.log.Timber

/**
 * Created by Ashish on Dec 29.
 */
class ReleaseTree : Timber.Tree() {

    companion object {

        private const val MAX_LOG_LENGTH = 4000

        private fun logMessage(priority: Int, tag: String, message: String) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
    }

    override fun log(priority: Int, tag: String, message: String, t: Throwable) {
        if (isLoggable(tag, priority)) {

            // Message is short enough, doesn't need to be broken into chunks
            if (message.length < MAX_LOG_LENGTH) {
                logMessage(priority, tag, message)
                return
            }
        }

        // Split by line, then ensure each line can fit into Log's maximum length
        var i = 0
        val length = message.length
        while (i < length) {
            var newLine = message.indexOf("\n", i)
            newLine = if (newLine != -1) newLine else length
            do {
                val end = Math.min(newLine, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                logMessage(priority, tag, part)
                i = end
            } while (i < newLine)

            i++
        }
    }
}