package com.ashish.movies.di.modules

import com.ashish.movies.data.api.MovieApi
import com.ashish.movies.data.api.PeopleApi
import com.ashish.movies.data.api.TVShowApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create<MovieApi>(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideTVShowApi(retrofit: Retrofit): TVShowApi = retrofit.create<TVShowApi>(TVShowApi::class.java)

    @Provides
    @Singleton
    fun providePeopleApi(retrofit: Retrofit): PeopleApi = retrofit.create<PeopleApi>(PeopleApi::class.java)
}