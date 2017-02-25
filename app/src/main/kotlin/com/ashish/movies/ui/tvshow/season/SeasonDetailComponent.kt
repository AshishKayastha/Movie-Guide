package com.ashish.movies.ui.tvshow.season

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface SeasonDetailComponent : AbstractComponent<SeasonDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<SeasonDetailActivity, SeasonDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}