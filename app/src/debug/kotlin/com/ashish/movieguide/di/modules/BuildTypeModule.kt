package com.ashish.movieguide.di.modules

import com.ashish.movieguide.di.qualifiers.BaseOkHttp
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
object BuildTypeModule {

    @Provides
    @Singleton
    @JvmStatic
    @BaseOkHttp
    fun provideBaseOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
        return okHttpClientBuilder.build()
    }
}