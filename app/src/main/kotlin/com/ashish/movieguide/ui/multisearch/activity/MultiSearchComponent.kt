package com.ashish.movieguide.ui.multisearch.activity

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class, MultiSearchFragmentBinder::class])
interface MultiSearchComponent : AbstractComponent<MultiSearchActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<MultiSearchActivity, MultiSearchComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}