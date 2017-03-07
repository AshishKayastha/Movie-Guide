package com.ashish.movieguide.ui.episode

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface EpisodeDetailComponent : AbstractComponent<EpisodeDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<EpisodeDetailActivity, EpisodeDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}