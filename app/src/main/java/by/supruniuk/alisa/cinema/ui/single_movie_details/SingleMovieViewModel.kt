package by.supruniuk.alisa.cinema.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.supruniuk.alisa.cinema.data.model.MovieDetails
import by.supruniuk.alisa.cinema.data.repository.MovieDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class SingleMovieViewModel (private  val repository: MovieDetailsRepository, movieId: Int ) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    val movieDetails : LiveData<MovieDetails> by lazy { //by lazy означает что мы получим данные только когда они нам нужны, а не когда этот класс будет инициализирован
        repository.fetchMovieDetails(movieId, scope)
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
