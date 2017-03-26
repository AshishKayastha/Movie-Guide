package com.ashish.movieguide.app

import android.app.Application
import com.ashish.movieguide.utils.DebugTree
import com.facebook.stetho.Stetho
import timber.log.Timber

abstract class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(DebugTree())
    }
}