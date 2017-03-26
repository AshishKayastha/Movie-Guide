package com.ashish.movieguide.di.modules

import com.ashish.movieguide.data.network.api.trakt.SyncApi
import com.ashish.movieguide.data.network.api.trakt.TraktAuthApi
import com.ashish.movieguide.data.network.api.trakt.TraktMovieApi
import com.ashish.movieguide.data.network.api.trakt.TraktShowApi
import com.ashish.movieguide.data.network.api.trakt.UserApi
import com.ashish.movieguide.di.qualifiers.Trakt
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object TraktApiModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideTraktAuthApi(@Trakt retrofit: Retrofit): TraktAuthApi = retrofit.create(TraktAuthApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideUserApi(@Trakt retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideTraktMovieApi(@Trakt retrofit: Retrofit): TraktMovieApi = retrofit.create(TraktMovieApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideTraktShowApi(@Trakt retrofit: Retrofit): TraktShowApi = retrofit.create(TraktShowApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideSyncApi(@Trakt retrofit: Retrofit): SyncApi = retrofit.create(SyncApi::class.java)
}