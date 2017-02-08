package com.ashish.movies.utils.extensions

import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Ashish on Feb 08.
 */
fun <T> rx.Observable<T>.toV2Observable(): Observable<T> = RxJavaInterop.toV2Observable(this)

fun <T> Observable<T>.observeOnMainThread(): Observable<T> = observeOn(AndroidSchedulers.mainThread())