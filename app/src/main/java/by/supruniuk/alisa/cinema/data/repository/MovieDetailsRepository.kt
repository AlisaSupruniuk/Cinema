package by.supruniuk.alisa.cinema.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.supruniuk.alisa.cinema.data.api.TheMovieDBInterface
import by.supruniuk.alisa.cinema.data.repository.NetworkState
import by.supruniuk.alisa.cinema.data.model.MovieDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsRepository (private  val  apiService : TheMovieDBInterface){

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieNetworkState = MutableLiveData<NetworkState>()

    fun fetchMovieDetails(id : Int, scope: CoroutineScope) : LiveData<MovieDetails> {

        movieNetworkState.postValue(NetworkState.LOADING)

        scope.launch(Dispatchers.IO) {
            val movieDetailsRequest = apiService.getMovieDetails(id)
            try {
                val response =movieDetailsRequest.await()
                val movieResponse = response.body()

                _movieDetails.postValue(movieResponse)

                movieNetworkState.postValue(NetworkState.LOADED)
            } catch (e: java.lang.Exception){
                movieNetworkState.postValue(NetworkState.ERROR)
            }
        }
        return _movieDetails
    }
}