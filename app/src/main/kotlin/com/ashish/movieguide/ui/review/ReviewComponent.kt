package com.ashish.movieguide.ui.review

import com.ashish.movieguide.di.modules.ActivityModule
import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.activity.ActivityComponentBuilder
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ReviewComponent : AbstractComponent<ReviewActivity> {

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<ReviewActivity, ReviewComponent> {
        fun withModule(module: ActivityModule): Builder
    }
}