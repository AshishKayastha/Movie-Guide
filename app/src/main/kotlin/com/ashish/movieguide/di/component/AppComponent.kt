package com.ashish.movieguide.di.component

import com.ashish.movieguide.app.MovieGuideApp
import com.ashish.movieguide.di.modules.ActivityBinders
import com.ashish.movieguide.di.modules.ApiModule
import com.ashish.movieguide.di.modules.AppModule
import com.ashish.movieguide.di.modules.NetModule
import com.ashish.movieguide.di.modules.TraktApiModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetModule::class,
        ApiModule::class,
        TraktApiModule::class,
        ActivityBinders::class
))
interface AppComponent : AbstractComponent<MovieGuideApp>