package com.ashish.movies.ui.multisearch

import android.app.Activity
import com.ashish.movies.data.api.SearchApi
import com.ashish.movies.utils.keyboardwatcher.KeyboardWatcher
import dagger.Module
import dagger.Provides

/**
 * Created by Ashish on Jan 05.
 */
@Module
class MultiSearchModule(private val activity: Activity) {

    @Provides
    fun provideMultiSearchPresenter(searchApi: SearchApi) = MultiSearchPresenter(searchApi)

    @Provides
    fun provideKeyboardWatcher() = KeyboardWatcher(activity)
}