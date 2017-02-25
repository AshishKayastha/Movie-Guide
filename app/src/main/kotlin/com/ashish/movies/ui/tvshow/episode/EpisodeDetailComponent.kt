package com.ashish.movies.ui.tvshow.episode

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface EpisodeDetailComponent : AbstractComponent<EpisodeDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<EpisodeDetailActivity, EpisodeDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}