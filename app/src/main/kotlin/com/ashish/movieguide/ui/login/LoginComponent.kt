package com.ashish.movieguide.ui.login

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface LoginComponent : AbstractComponent<LoginActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<LoginActivity, LoginComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}