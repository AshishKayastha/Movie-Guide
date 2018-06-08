package com.ashish.movieguide.di.component

import android.app.Application
import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.di.modules.ActivityBindingModule
import com.ashish.movieguide.di.modules.ApiModule
import com.ashish.movieguide.di.modules.AppModule
import com.ashish.movieguide.di.modules.NetModule
import com.ashish.movieguide.di.modules.TraktApiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    NetModule::class,
    ApiModule::class,
    TraktApiModule::class,
    ActivityBindingModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<MovieGuideApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}