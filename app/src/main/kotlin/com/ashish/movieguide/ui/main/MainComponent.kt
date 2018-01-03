package com.ashish.movieguide.ui.main

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class, MainFragmentBinders::class])
interface MainComponent : AbstractComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<MainActivity, MainComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}