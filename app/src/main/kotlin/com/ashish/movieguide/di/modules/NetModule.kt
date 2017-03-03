package com.ashish.movieguide.di.modules

import com.ashish.movieguide.BuildConfig
import com.ashish.movieguide.di.qualifiers.BaseOkHttp
import com.ashish.movieguide.utils.ApiKeyInterceptor
import com.ashish.movieguide.utils.Constants.OMDB_API_BASE_URL
import com.ashish.movieguide.utils.Constants.TMDB_API_BASE_URL
import com.ashish.movieguide.utils.schedulers.BaseSchedulerProvider
import com.ashish.movieguide.utils.schedulers.SchedulerProvider
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Ashish on Dec 27.
 */
@Module
object NetModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }
        loggingInterceptor.level = if (BuildConfig.DEBUG) BODY else NONE
        return loggingInterceptor
    }

    @Provides
    @Singleton
    @JvmStatic
    @BaseOkHttp
    fun provideBaseOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(30, SECONDS)
                .readTimeout(30, SECONDS)
                .writeTimeout(30, SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor()

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