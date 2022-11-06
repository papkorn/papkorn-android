package com.nuller.popkorn.utils

import com.nuller.popkorn.ResponseHomePage
import com.nuller.popkorn.reponses.*
import retrofit2.Call
import retrofit2.http.*

interface API {
    companion object {
        //        const val BASE_URL = "https://api.papkornbot.ir/api/"
        const val BASE_URL = "https://api.pkdirectdl.xyz/api/"

        //        const val BASE_URL = "https://papkorn.3sigma.ir/api/"
        const val ACCEPT_TYPE = "application/json"
        const val CSRF_TOKEN = "bnZyOewsJchpvDINhswdNZ8X02MmG4HWBl7LT1UKdUCKFFwCX9bulgTr9s6JtNWi"
    }

    @GET("gemovies_app_version")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getAppUpdates(): Call<ResponseAppUpdate>

    @GET("home_page/rows")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getHomePageRows(): Call<ResponseHomePage>

    @GET
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getMoviesByUrl(@Url searchUrl: String): Call<ResponseMovieByUrl>

    @GET("genre/")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getGenre(): Call<List<ResponseGenreItem>>

    @GET("search/movie/")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun searchMovie(
        @Query("movie_type") movieType: String,
        @Query("genre") genre: String,
        @Query("year__gte") yearGte: Int,
        @Query("total_download_link__gt") totalDownloadLinkGt: Int,
        @Query("ordering") ordering: String
    )

    @GET("movie/imdb_id/{imdb_id}")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getMovie(@Path("imdb_id") imdbId: Int): Call<ResponseMovie>

    @GET("movie/{imdb_id}/similar")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getSimilarMovies(
        @Path("imdb_id") imdbId: Int,
        @Query("limit") limit: Int
    ): Call<ResponseMovieByUrl>

    @GET("movie/{imdb_id}/recommend")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getRecommendMovies(
        @Path("imdb_id") imdbId: Int,
        @Query("limit") limit: Int
    ): Call<ResponseMovieByUrl>


    @GET("movie/{imdb_id}/images")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getMovieImages(
        @Path("imdb_id") imdbId: Int,
        @Query("limit") limit: Int
    ): Call<ResponseMovieImages>

    @GET("movie/{imdb_id}/links")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getMovieLinks(@Path("imdb_id") imdbId: Int): Call<List<ResponseMovieLinksItem>>

    @GET("series/{imdb_id}/links")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getSerieLinks(
        @Path("imdb_id") imdbId: Int,
        @Query("season_no") seasonNumber: Int,
        @Query("episode_no") episodeNumber: Int
    ): Call<List<ResponseMovieLinksItem>>

    @GET("movie/{imdb_id}/subtitle")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getMovieSubtitles(
        @Path("imdb_id") imdbId: Int
    ): Call<List<ResponseMovieSubtitlesItem>>

    @GET("series/{imdb_id}/subtitle")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getSerieSubtitles(
        @Path("imdb_id") imdbId: Int,
        @Query("season_no") seasonNumber: Int,
        @Query("episode_no") episodeNumber: Int
    ): Call<List<ResponseMovieSubtitlesItem>>

    @GET("series/{imdb_id}/seasons")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getSerieSeasons(@Path("imdb_id") imdbId: Int): Call<List<ResponseSerieSeasonsItem>>

    @GET("series/{imdb_id}/episodes")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun getSerieEpisodes(
        @Path("imdb_id") imdbId: Int,
        @Query("season_no") seasonNumber: Int
    ): Call<List<ResponseSerieEpisodesItem>>

    @GET("{type}/?ordering=-year")
    @Headers("accept:$ACCEPT_TYPE,X-CSRFToken:$CSRF_TOKEN")
    fun search(
        @Path("type") movieType: String,
        @Query("search") search: String
    ): Call<ResponseMovieByUrl>

}