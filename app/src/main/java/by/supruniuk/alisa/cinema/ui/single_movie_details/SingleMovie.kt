package by.supruniuk.alisa.cinema.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import by.supruniuk.alisa.cinema.R
import by.supruniuk.alisa.cinema.data.api.POSTER_BASE_URL
import by.supruniuk.alisa.cinema.data.api.TheMovieDBClient
import by.supruniuk.alisa.cinema.data.api.TheMovieDBInterface
import by.supruniuk.alisa.cinema.data.repository.NetworkState
import by.supruniuk.alisa.cinema.data.model.MovieDetails
import by.supruniuk.alisa.cinema.data.repository.MovieDetailsRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private  lateinit var viewModel: SingleMovieViewModel
    private  lateinit var repository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient() //получаем клиента в apiService
        repository = MovieDetailsRepository(apiService) //инициализируем MovieDetailsRepository путем передачи apiService

        viewModel = getViewModel(movieId) //получаем viewModel вызывая функцию и передавая в нее id

        viewModel.movieDetails.observe(this, Observer {
            //получаем данные о фильме как livedata
            bindUI(it) //и устанавливаем их //если произойдут какие либо изменения мы можем обновить наш UI здесь
        })

        repository.movieNetworkState.observe(this, Observer {
            onMovieNetworkStateChanged(it)
        })
    }

    fun bindUI(it: MovieDetails){
        tv_movie_title.text = it.title
        tv_movie_tagline.text = it.tagline
        tv_movie_release_date.text = it.releaseDate
        tv_movie_rating.text = it.rating.toString()
        tv_movie_runtime.text = it.runtime.toString() + " мин."
        tv_movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US) //формат валюты для бюджета и сборов
        tv_movie_budget.text = formatCurrency.format(it.budget)
        tv_movie_revenue.text = formatCurrency.format(it.revenue)

        //объединяем пути POSTER_BASE_URL с базовым url для получения полного url адреса
        val moviePosterURL: String = POSTER_BASE_URL + it.posterPath
        // библиотека Glide предназначена для асинхронной подгрузки изображений из сети, ресурсов или файловой системы, их кэширования и отображения
        Glide.with(this)
            .load(moviePosterURL) //откуда
            .into(iv_movie_poster); //куда
    }

    private fun onMovieNetworkStateChanged(state: NetworkState) {

        when (state) {
            NetworkState.LOADING -> {
                progress_bar.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                progress_bar.visibility = View.GONE
            }
            NetworkState.ERROR -> {
                tv_error.visibility = View.VISIBLE
                tv_error.text = "Ошибка"
                progress_bar.visibility = View.GONE
            }
        }
    }

    private  fun getViewModel(movieId: Int): SingleMovieViewModel {

        return  ViewModelProviders.of(this, object  : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress ("UNCHECKED_CAST")
                return SingleMovieViewModel(repository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}


