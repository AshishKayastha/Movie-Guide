package com.ashish.movies.di.modules

import com.ashish.movies.data.api.AuthApi
import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.api.OMDbApi
import com.ashish.movies.data.api.PeopleApi
import com.ashish.movies.data.api.SearchApi
import com.ashish.movies.data.api.TVShowApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Module
object ApiModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideTVShowApi(retrofit: Retrofit): TVShowApi = retrofit.create(TVShowApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun providePeopleApi(retrofit: Retrofit): PeopleApi = retrofit.create(PeopleApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideSearchApi(retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    fun provideOMDbApi(@Named("omdb") retrofit: Retrofit): OMDbApi = retrofit.create(OMDbApi::class.java)
}