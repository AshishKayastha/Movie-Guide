package com.ashish.movieguide.ui.profile

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import com.ashish.movieguide.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ProfileComponent : AbstractComponent<ProfileActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<ProfileActivity, ProfileComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}