package com.ashish.movieguide.di.multibindings.activity

import android.app.Activity

import com.ashish.movieguide.di.multibindings.AbstractComponent
import com.ashish.movieguide.di.multibindings.ComponentBuilder

interface ActivityComponentBuilder<in A : Activity, out C : AbstractComponent<A>> : ComponentBuilder<A, C>