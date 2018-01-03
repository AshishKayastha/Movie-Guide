package com.ashish.movieguide.utils

interface BiFunction<in T1, in T2, out R> {
    fun apply(var1: T1, var2: T2?): R
}