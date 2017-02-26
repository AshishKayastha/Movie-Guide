package com.ashish.movieguide.di.multibindings

interface AbstractComponent<in T> {

    fun inject(injectTarget: T)
}