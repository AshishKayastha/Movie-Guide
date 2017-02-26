package com.ashish.movieguide.ui.movie.detail

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface MovieDetailComponent : AbstractComponent<MovieDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<MovieDetailActivity, MovieDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}