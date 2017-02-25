package com.ashish.movies.di.multibindings.activity

import android.app.Activity

import com.ashish.movies.di.multibindings.AbstractComponent
import com.ashish.movies.di.multibindings.ComponentBuilder

interface ActivityComponentBuilder<in A : Activity, out C : AbstractComponent<A>> : ComponentBuilder<A, C>