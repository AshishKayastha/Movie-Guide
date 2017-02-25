package com.ashish.movies.ui.multisearch.activity

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        MultiSearchFragmentBinder::class
))
interface MultiSearchComponent : AbstractComponent<MultiSearchActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<MultiSearchActivity, MultiSearchComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}