package com.ashish.movieguide.ui.season

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface SeasonDetailComponent : AbstractComponent<SeasonDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<SeasonDetailActivity, SeasonDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}