package com.ashish.movies.ui.tvshow.detail

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface TVShowDetailComponent : AbstractComponent<TVShowDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<TVShowDetailActivity, TVShowDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}