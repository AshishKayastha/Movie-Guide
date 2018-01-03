package com.ashish.movieguide.ui.tvshow.detail

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TVShowDetailComponent : AbstractComponent<TVShowDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<TVShowDetailActivity, TVShowDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}