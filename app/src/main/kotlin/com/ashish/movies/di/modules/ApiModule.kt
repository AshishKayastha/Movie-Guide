package com.ashish.movies.di.modules

import com.ashish.movies.data.api.MovieService
import com.ashish.movies.data.api.TVShowService
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
    fun provideMovieService(retrofit: Retrofit): MovieService
            = retrofit.create<MovieService>(MovieService::class.java)

    @Provides
    @Singleton
    fun provideTVShowService(retrofit: Retrofit): TVShowService
            = retrofit.create<TVShowService>(TVShowService::class.java)
}