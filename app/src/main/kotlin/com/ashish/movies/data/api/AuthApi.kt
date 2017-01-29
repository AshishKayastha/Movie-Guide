package com.ashish.movies.data.api

import com.ashish.movies.data.models.Account
import com.ashish.movies.data.models.RequestToken
import com.ashish.movies.data.models.Session
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ashish on Jan 28.
 */
interface AuthApi {

    @GET("authentication/token/new")
    fun createRequestToken(): Observable<RequestToken>

    @GET("authentication/session/new")
    fun createSession(@Query("request_token") requestToken: String): Observable<Session>

    @GET("account")
    fun getUserAccount(@Query("session_id") sessionId: String): Observable<Account>
}