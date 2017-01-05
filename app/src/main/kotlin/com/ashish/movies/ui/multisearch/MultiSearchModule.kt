package com.ashish.movies.ui.multisearch

import com.ashish.movies.data.api.SearchApi
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 05.
 */
@Module
class MultiSearchModule {

    @Provides
    fun provideMultiSearchPresenter(searchApi: SearchApi) = MultiSearchPresenter(searchApi)
}