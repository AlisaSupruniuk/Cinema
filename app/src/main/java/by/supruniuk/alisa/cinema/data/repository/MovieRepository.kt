package by.supruniuk.alisa.cinema.data.repository

import by.supruniuk.alisa.cinema.data.api.TheMovieDBInterface
import by.supruniuk.alisa.cinema.data.model.Movie

class MovieRepository (private  val apiService: TheMovieDBInterface){

    suspend fun fetchPopularMovies(): List<Movie>? {
        val deferredResponse = apiService.getPopularMovies().await()

        return if (deferredResponse.isSuccessful) {
            deferredResponse.body()?.movieList
        } else {
            throw Exception()
        }
    }

    suspend fun fetchMovieByQuery(query: String) : List<Movie>? {
        val deferredResponse = apiService.getSearchMovie(query).await()

        return if (deferredResponse.isSuccessful){
            deferredResponse.body()?.movieList
        } else {
            throw Exception()
        }
    }


}