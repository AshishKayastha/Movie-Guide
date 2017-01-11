package com.ashish.movies.di.modules

import com.ashish.movies.BuildConfig
import com.ashish.movies.utils.ApiConstants.OMDB_BASE_API_URL
import com.ashish.movies.utils.ApiConstants.TMDB_BASE_API_URL
import com.ashish.movies.utils.ApiKeyInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
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
        return OkHttpClient.Builder()
                .connectTimeout(30, SECONDS)
                .readTimeout(30, SECONDS)
                .writeTimeout(30, SECONDS)
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor()

    @Provides
    @Singleton
    fun provideTMDbClient(builder: OkHttpClient.Builder, apiKeyInterceptor: Interceptor,
                          loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return builder.addNetworkInterceptor(apiKeyInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @Named("omdb")
    fun provideOMDbClient(builder: OkHttpClient.Builder, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return builder.addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory
            = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
                        callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TMDB_BASE_API_URL)
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    @Named("omdb")
    fun provideOMDbRetrofit(@Named("omdb") client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
                            callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(OMDB_BASE_API_URL)
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }
}