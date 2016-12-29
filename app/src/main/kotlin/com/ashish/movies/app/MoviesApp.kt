package com.ashish.movies.app

import android.app.Application
import android.content.Context
import com.ashish.movies.di.components.AppComponent
import com.ashish.movies.di.components.DaggerAppComponent

/**
 * Created by Ashish on Dec 28.
 */
class MoviesApp : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            val app = context as MoviesApp
            return app.appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}