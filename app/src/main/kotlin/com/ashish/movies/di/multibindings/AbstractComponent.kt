package com.ashish.movies.di.multibindings

interface AbstractComponent<in T> {

    fun inject(injectTarget: T)
}