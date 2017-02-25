package com.ashish.movies.ui.people.detail

import com.ashish.movies.di.modules.ActivityModule
import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movies.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface PersonDetailComponent : AbstractComponent<PersonDetailActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<PersonDetailActivity, PersonDetailComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}