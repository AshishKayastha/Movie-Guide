package com.ashish.movies.di.modules

import com.ashish.movies.BuildConfig
import com.ashish.movies.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Module
class NetModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) BODY else NONE
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, SECONDS)
        builder.readTimeout(30, SECONDS)
        builder.writeTimeout(30, SECONDS)
        return builder
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideClient(builder: OkHttpClient.Builder, interceptor: HttpLoggingInterceptor): OkHttpClient {
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .client(client)
                .build()
    }
}