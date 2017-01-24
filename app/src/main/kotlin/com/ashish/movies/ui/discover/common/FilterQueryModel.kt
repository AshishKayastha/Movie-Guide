package com.ashish.movies.ui.discover.common

import com.ashish.movies.data.models.FilterQuery
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ashish on Jan 24.
 */
@Singleton
class FilterQueryModel @Inject constructor() {

    private val filterQuerySubject = PublishSubject.create<FilterQuery>()

    fun setFilterQuery(filterQuery: FilterQuery) = filterQuerySubject.onNext(filterQuery)

    fun getFilterQuery(): Observable<FilterQuery> = filterQuerySubject
}