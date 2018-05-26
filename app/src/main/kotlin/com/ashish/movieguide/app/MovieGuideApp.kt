package com.ashish.movieguide.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.ashish.movieguide.di.component.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by Ashish on Dec 28.
 */
class MovieGuideApp : BaseApp(), HasActivityInjector {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getRefWatcher(context: Context) = (context.applicationContext as MovieGuideApp).refWatcher
    }

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var refWatcher: RefWatcher

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        refWatcher = LeakCanary.install(this)

        context = this
        initDagger()
    }

    private fun initDagger() {
        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()

        appComponent.inject(this)
    }

    override fun activityInjector() = dispatchingActivityInjector
}