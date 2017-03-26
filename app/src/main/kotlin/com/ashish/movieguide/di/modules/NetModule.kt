package com.ashish.movieguide.di.modules

import android.content.Context
import android.util.Log
import com.ashish.movieguide.BuildConfig
import com.ashish.movieguide.di.qualifiers.ApplicationContext
import com.ashish.movieguide.di.qualifiers.BaseOkHttp
import com.ashish.movieguide.di.qualifiers.OMDb
import com.ashish.movieguide.di.qualifiers.Trakt
import com.ashish.movieguide.utils.Constants.OMDB_API_BASE_URL
import com.ashish.movieguide.utils.OfflineCacheInterceptor
import com.ashish.movieguide.utils.TMDbApiKeyInterceptor
import com.ashish.movieguide.utils.TMDbConstants.TMDB_API_BASE_URL
import com.ashish.movieguide.utils.TraktAuthenticator
import com.ashish.movieguide.utils.TraktConstants.TRAKT_API_BASE_URL
import com.ashish.movieguide.utils.Traktnterceptor
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import com.ashish.movieguide.utils.schedulers.SchedulerProvider
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS
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
    fun provideBaseOkHttpClient(cache: Cache, loggingInterceptor: LoggingInterceptor,
                                offlineCacheInterceptor: OfflineCacheInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(20, SECONDS)
                .readTimeout(20, SECONDS)
                .writeTimeout(20, SECONDS)
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(offlineCacheInterceptor)
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideTMDbClient(@BaseOkHttp okHttpClient: OkHttpClient,
                          apiKeyInterceptor: TMDbApiKeyInterceptor): OkHttpClient {
        return okHttpClient.newBuilder()
                .addInterceptor(apiKeyInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    @JvmStatic
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory
            = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    @JvmStatic
    fun provideTMDbRetrofit(client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
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
    @OMDb
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
    @Trakt
    fun provideTraktClient(@BaseOkHttp okHttpClient: OkHttpClient, traktnterceptor: Traktnterceptor,
                           traktAuthenticator: TraktAuthenticator): OkHttpClient {
        return okHttpClient.newBuilder()
                .addNetworkInterceptor(traktnterceptor)
                .authenticator(traktAuthenticator)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    @Trakt
    fun provideTraktRetrofit(@Trakt client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory,
                             callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TRAKT_API_BASE_URL)
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideschedulers(schedulerProvider: SchedulerProvider): BaseSchedulerProvider = schedulerProvider
}