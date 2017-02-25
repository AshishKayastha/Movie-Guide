package com.ashish.movies.di.components

import com.ashish.movies.di.modules.ApiModule
import com.ashish.movies.di.modules.AppModule
import com.ashish.movies.di.modules.NetModule
import com.ashish.movies.di.modules.UiModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetModule::class,
        ApiModule::class
))
interface AppComponent {

    fun plus(uiModule: UiModule): UiComponent
}