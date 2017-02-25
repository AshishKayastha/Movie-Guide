package com.ashish.movies.utils

import com.ashish.movies.BuildConfig
import timber.log.Timber

/**
 * Created by Ashish on Feb 25.
 */
object Logger {

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    fun v(msg: String, vararg objects: Any) = Timber.v(msg, *objects)

    fun d(msg: String, vararg objects: Any) = Timber.d(msg, *objects)

    fun i(msg: String, vararg objects: Any) = Timber.i(msg, *objects)

    fun e(throwable: Throwable) = Timber.e(throwable)

    fun e(throwable: Throwable, msg: String, vararg objects: Any) {
        Timber.e(throwable, msg, *objects)
    }
}