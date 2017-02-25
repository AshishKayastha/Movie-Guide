package com.ashish.movies.ui.discover.filter

import com.ashish.movies.data.models.FilterQuery
import com.ashish.movies.di.scopes.FragmentScope
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by Ashish on Jan 24.
 */
@FragmentScope
class FilterQueryModel @Inject constructor() {

    private val filterQuerySubject = PublishSubject.create<FilterQuery>()

    fun setFilterQuery(filterQuery: FilterQuery) = filterQuerySubject.onNext(filterQuery)

    fun getFilterQuery(): Observable<FilterQuery> = filterQuerySubject
}