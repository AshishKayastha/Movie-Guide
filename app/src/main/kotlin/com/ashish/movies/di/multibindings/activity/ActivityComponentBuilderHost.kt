package com.ashish.movies.di.multibindings.activity

import android.app.Activity
import com.ashish.movies.di.multibindings.AbstractComponent

interface ActivityComponentBuilderHost {

    fun <A : Activity, B : ActivityComponentBuilder<A, AbstractComponent<A>>>
            getActivityComponentBuilder(activityKey: Class<A>, builderType: Class<B>): B
}