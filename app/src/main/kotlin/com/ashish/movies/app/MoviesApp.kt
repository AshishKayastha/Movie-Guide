package com.ashish.movies.app

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.ashish.movies.BuildConfig
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.di.components.DaggerAppComponent
import com.ashish.movies.utils.ReleaseTree
import timber.log.Timber

/**
 * Created by Ashish on Dec 28.
 */
class MoviesApp : Application() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as MoviesApp
            return app.appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    private fun initTimber() {
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
}