package com.ashish.movieguide.data.api.tmdb

import com.ashish.movieguide.data.models.Account
import com.ashish.movieguide.data.models.Episode
import com.ashish.movieguide.data.models.Favorite
import com.ashish.movieguide.data.models.Movie
import com.ashish.movieguide.data.models.RequestToken
import com.ashish.movieguide.data.models.Results
import com.ashish.movieguide.data.models.Session
import com.ashish.movieguide.data.models.Status
import com.ashish.movieguide.data.models.TVShow
import com.ashish.movieguide.data.models.Watchlist
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
    ): Observable<Status>

    @POST("account/{accountId}/watchlist")
    fun addToWatchlist(
            @Path("accountId") accountId: Long,
            @Body watchlist: Watchlist
    ): Observable<Status>

    @GET("account/{accountId}/{type}/movies")
    fun getPersonalMoviesByType(
            @Path("type") type: String,
            @Path("accountId") accountId: Long,
            @Query("page") page: Int = 1
    ): Observable<Results<Movie>>

    @GET("account/{accountId}/{type}/tv")
    fun getPersonalTVShowsByType(
            @Path("type") type: String,
            @Path("accountId") accountId: Long,
            @Query("page") page: Int = 1
    ): Observable<Results<TVShow>>

    @GET("account/{accountId}/rated/movies")
    fun getRatedMovies(
            @Path("accountId") accountId: Long,
            @Query("page") page: Int = 1
    ): Observable<Results<Movie>>

    @GET("account/{accountId}/rated/tv")
    fun getRatedTVShows(
            @Path("accountId") accountId: Long,
            @Query("page") page: Int = 1
    ): Observable<Results<TVShow>>

    @GET("account/{accountId}/rated/tv/episodes")
    fun getRatedEpisodes(
            @Path("accountId") accountId: Long,
            @Query("page") page: Int = 1
    ): Observable<Results<Episode>>
}