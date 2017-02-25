package com.ashish.movies.di.multibindings

interface ComponentBuilder<in T, out C : AbstractComponent<T>> {

    fun build(): C
}