package com.ashish.movies.di.component

import com.ashish.movies.app.MoviesApp
import com.ashish.movies.di.modules.ActivityBinders
import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.AppModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.di.multibindings.AbstractComponent
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
        ActivityBinders::class
))
interface AppComponent : AbstractComponent<MoviesApp>