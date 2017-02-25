package com.ashish.movies.utils.schedulers

import io.reactivex.Scheduler

/**
 * Created by Ashish on Feb 23.
 */
interface BaseSchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler
}