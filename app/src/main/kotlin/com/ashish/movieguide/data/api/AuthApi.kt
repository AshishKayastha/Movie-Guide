package com.ashish.movieguide.data.api

import com.ashish.movieguide.data.models.Account
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.RequestToken
import com.ashish.movieguide.data.models.Session
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @POST("account/{accountId}/favorite")
    fun markAsFavorite(
            @Path("accountId") accountId: Long,
            @Body favorite: Favorite
    ): Completable
}