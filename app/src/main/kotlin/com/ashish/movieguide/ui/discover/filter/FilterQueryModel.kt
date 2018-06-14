package com.ashish.movieguide.ui.discover.filter

import com.ashish.movieguide.data.remote.entities.tmdb.FilterQuery
import com.ashish.movieguide.di.scopes.FragmentScope
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by Ashish on Jan 24.
 */
@FragmentScope
class FilterQueryModel @Inject constructor() {

    private val filterQuerySubject = PublishSubject.create<FilterQuery>()

    fun getFilterQuery(): Observable<FilterQuery> = filterQuerySubject

    fun setFilterQuery(filterQuery: FilterQuery) = filterQuerySubject.onNext(filterQuery)
}