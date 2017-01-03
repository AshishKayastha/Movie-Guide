package com.ashish.movies.data.models

/**
 * Created by Ashish on Jan 03.
 */
data class DetailResults<out I>(val item: I?, val creditResults: CreditResults?, val similarItems: List<I>?)