package by.supruniuk.alisa.cinema.data.api

import by.supruniuk.alisa.cinema.data.model.MovieDetails
import by.supruniuk.alisa.cinema.data.model.MovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//https://api.themoviedb.org/3/movie/528085?api_key=ad16c8e0e0197a82b75db19a22aa0578
//https://api.themoviedb.org/3/movie/popular?api_key=ad16c8e0e0197a82b75db19a22aa0578&page=1
//https://api.themoviedb.org/3/   //base URL for Retrofit

//интерфейс с запросами
interface TheMovieDBInterface{

    @GET("movie/popular")
    fun getPopularMovies() : Deferred<Response<MovieResponse>>

    @GET("movie/{movie_id}")  // команда на сервере
    fun getMovieDetails(@Path("movie_id") id:Int): Deferred<Response<MovieDetails>>

    @GET("search/movie")
    fun  getSearchMovie(@Query("query") query: String) : Deferred<Response<MovieResponse>>

}
