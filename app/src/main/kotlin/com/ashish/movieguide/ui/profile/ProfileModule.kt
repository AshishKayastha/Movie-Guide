package com.ashish.movieguide.ui.profile

import android.content.Context
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class ProfileModule {

    @Binds
    abstract fun provideContext(activity: ProfileActivity): Context
}