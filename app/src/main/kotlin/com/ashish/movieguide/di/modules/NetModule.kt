package com.ashish.movieguide.di.modules

import android.content.Context
import android.util.Log
import com.ashish.movieguide.BuildConfig
import com.ashish.movieguide.di.qualifiers.ApplicationContext
import com.ashish.movieguide.di.qualifiers.BaseOkHttp
import com.ashish.movieguide.utils.ApiKeyInterceptor
import com.ashish.movieguide.utils.Constants.OMDB_API_BASE_URL
import com.ashish.movieguide.utils.Constants.TMDB_API_BASE_URL
import com.ashish.movieguide.utils.OfflineCacheInterceptor
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import com.ashish.movieguide.utils.schedulers.SchedulerProvider
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Module
object NetModule {

    private const val CACHE_DIRECTORY = "http-cache"
    private const val CACHE_SIZE = 20 * 1024 * 1024L // 20MB

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Log.DEBUG)
                .request("Request")
                .response("Response")
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir, CACHE_DIRECTORY), CACHE_SIZE)
    }

    @Provides
    @Singleton
    @JvmStatic
    @BaseOkHttp
    fun provideBaseOkHttpClient(cache: Cache, loggingInterceptor: LoggingInterceptor,
                                offlineCacheInterceptor: OfflineCacheInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(30, SECONDS)
                .readTimeout(30, SECONDS)
                .writeTimeout(30, SECONDS)
                .cache(cache)
                .addInterceptor(offlineCacheInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideApiKeyInterceptor(apiKeyInterceptor: ApiKeyInterceptor): Interceptor = apiKeyInterceptor

    @Provides
    @Singleton
    @JvmStatic
    fun provideTMDbClient(@BaseOkHttp okHttpClient: OkHttpClient, apiKeyInterceptor: Interceptor): OkHttpClient {
        return okHttpClient.newBuilder()
                .addNetworkInterceptor(apiKeyInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    @JvmStatic
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory
            = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
                        callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TMDB_API_BASE_URL)
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    @Named("omdb")
    fun provideOMDbRetrofit(@BaseOkHttp client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
                            callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(OMDB_API_BASE_URL)
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun schedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider = schedulerProvider
}