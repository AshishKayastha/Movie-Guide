package com.ashish.movieguide.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import java.util.Collections
import javax.inject.Singleton

@Module
object BuildTypeModule {

    @Provides
    @Singleton
    @JvmStatic
    @BaseOkHttp
    fun provideBaseOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpClientBuilder.build()
    }
}