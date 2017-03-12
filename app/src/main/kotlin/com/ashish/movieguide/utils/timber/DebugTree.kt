package com.ashish.movieguide.utils.timber

import timber.log.Timber

/**
 * A [Timber.Tree] for debug builds. This will log messages along with line numbers.
 */
open class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}