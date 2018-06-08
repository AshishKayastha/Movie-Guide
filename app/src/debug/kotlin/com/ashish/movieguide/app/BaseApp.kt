package com.ashish.movieguide.app

import android.app.Application
import com.ashish.movieguide.utils.DebugTree
import timber.log.Timber

abstract class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}