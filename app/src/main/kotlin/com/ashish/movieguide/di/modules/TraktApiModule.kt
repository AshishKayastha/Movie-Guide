package com.ashish.movieguide.di.modules

import com.ashish.movieguide.data.api.trakt.TraktAuthApi
import com.ashish.movieguide.data.api.trakt.UserApi
import com.ashish.movieguide.di.qualifiers.Trakt
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object TraktApiModule {

    @Provides @Singleton @JvmStatic
    fun provideTraktAuthApi(@Trakt retrofit: Retrofit): TraktAuthApi = retrofit.create(TraktAuthApi::class.java)

    @Provides @Singleton @JvmStatic
    fun provideUserApi(@Trakt retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}