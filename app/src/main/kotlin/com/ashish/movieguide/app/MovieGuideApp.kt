package com.ashish.movieguide.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.ashish.movieguide.di.component.DaggerAppComponent
import com.ashish.movieguide.di.modules.AppModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilderHost
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ashish on Dec 28.
 */
class MovieGuideApp : BaseApp(), ActivityComponentBuilderHost {

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun getRefWatcher(context: Context) = (context.applicationContext as MovieGuideApp).refWatcher
    }

    @Inject
    lateinit var componentBuilders: Map<Class<out Activity>, @JvmSuppressWildcards Provider<ActivityComponentBuilder<*, *>>>

    private lateinit var refWatcher: RefWatcher

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        refWatcher = LeakCanary.install(this)

        context = this
        initDagger()
    }

    private fun initDagger() {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
                .inject(this)
    }

    override fun <A : Activity, B : ActivityComponentBuilder<A, AbstractComponent<A>>>
            getActivityComponentBuilder(activityKey: Class<A>, builderType: Class<B>): B {
        return builderType.cast(componentBuilders[activityKey]!!.get())
    }
}