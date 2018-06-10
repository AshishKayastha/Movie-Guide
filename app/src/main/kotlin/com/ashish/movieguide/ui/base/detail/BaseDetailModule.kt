package com.ashish.movieguide.ui.base.detail

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module

@Module
abstract class BaseDetailModule<A : AppCompatActivity> {

    @Binds
    abstract fun provideAppCompatActivity(activity: A): Activity
}