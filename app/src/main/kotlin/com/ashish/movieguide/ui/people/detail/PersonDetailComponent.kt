package com.ashish.movieguide.ui.people.detail

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface PersonDetailComponent : AbstractComponent<PersonDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<PersonDetailActivity, PersonDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}