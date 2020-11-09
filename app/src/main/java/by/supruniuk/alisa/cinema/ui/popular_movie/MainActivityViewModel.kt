package by.supruniuk.alisa.cinema.ui.popular_movie

import androidx.lifecycle.*
import by.supruniuk.alisa.cinema.data.repository.NetworkState
import by.supruniuk.alisa.cinema.data.model.Movie
import by.supruniuk.alisa.cinema.data.repository.MovieRepository
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivityViewModel(private  val repository : MovieRepository) : ViewModel() {

    private var searchJob: Job? = null

    private var searchMoviesLiveData : LiveData<List<Movie>>
    private val popularMoviesLiveData = MutableLiveData<List<Movie>>()

    val moviesMediatorLiveData = MediatorLiveData<List<Movie>>()

    private val searchMovieFieldTextLiveData = MutableLiveData<String>()

    val networkStateLiveData = MutableLiveData<NetworkState>()


    init {
        searchMoviesLiveData = Transformations.switchMap(searchMovieFieldTextLiveData)
        {
            fetchMovieByQuery(it)
        }

        moviesMediatorLiveData.addSource(popularMoviesLiveData){
            moviesMediatorLiveData.value = it
        }

        moviesMediatorLiveData.addSource(searchMoviesLiveData){
            moviesMediatorLiveData.value = it
        }
    }

    fun fetchPopularMovie(){

        networkStateLiveData.value = NetworkState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = repository.fetchPopularMovies()
                popularMoviesLiveData.postValue(movies)

                networkStateLiveData.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                networkStateLiveData.postValue(NetworkState.ERROR)
            }
        }
    }

    fun onSearchQuery(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.length > 2){
                searchMovieFieldTextLiveData.value = query
            }
        }
    }

    private fun fetchMovieByQuery(query: String) : LiveData<List<Movie>> {

        val liveData = MutableLiveData<List<Movie>>()

        networkStateLiveData.value = NetworkState.LOADING
        viewModelScope.launch {
          try {
              val movies = repository.fetchMovieByQuery(query)
              liveData.postValue(movies)

              networkStateLiveData.postValue(NetworkState.LOADED)

          }  catch (E:Exception){
              networkStateLiveData.postValue(NetworkState.ERROR)
          }
        }
        return liveData
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}