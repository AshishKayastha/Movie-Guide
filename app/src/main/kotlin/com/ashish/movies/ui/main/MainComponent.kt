package com.ashish.movies.ui.main

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        MainFragmentBinders::class
))
interface MainComponent : AbstractComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<MainActivity, MainComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}