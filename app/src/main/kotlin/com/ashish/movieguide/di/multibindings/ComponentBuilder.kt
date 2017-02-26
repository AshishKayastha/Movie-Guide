package com.ashish.movieguide.di.multibindings

interface ComponentBuilder<in T, out C : AbstractComponent<T>> {

    fun build(): C
}