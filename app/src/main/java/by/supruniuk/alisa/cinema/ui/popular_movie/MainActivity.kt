package by.supruniuk.alisa.cinema.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import by.supruniuk.alisa.cinema.R
import by.supruniuk.alisa.cinema.data.api.TheMovieDBClient
import by.supruniuk.alisa.cinema.data.api.TheMovieDBInterface
import by.supruniuk.alisa.cinema.data.repository.MovieAdapter
import by.supruniuk.alisa.cinema.data.repository.MovieRepository
import by.supruniuk.alisa.cinema.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
    var movieRepository = MovieRepository(apiService)
    var movieAdapter = MovieAdapter(this)

    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            viewModel.onSearchQuery(editable.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel()
        viewModel.fetchPopularMovie()

        //макет сетки и параметры - это количество промежутков
        val gridLayoutManager = GridLayoutManager(this, 2) //spanCount: 3 //было так

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        et_search.addTextChangedListener(searchTextWatcher)

        viewModel.moviesMediatorLiveData.observe(this, Observer {
            movieAdapter.updateData(it)
        })

         viewModel.networkStateLiveData.observe(this, Observer {
            onMovieNetworkStateChanged(it)
        })

    }

    private fun onMovieNetworkStateChanged(state: NetworkState) {

        when (state) {
            NetworkState.LOADING -> {
                rv_movie_list.visibility = View.GONE
                progress_bar_popular.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                rv_movie_list.visibility = View.VISIBLE
                progress_bar_popular.visibility = View.GONE
            }
            NetworkState.ERROR -> {
                tv_error_popular.visibility = View.VISIBLE
                tv_error_popular.text = "Ошибка"
                rv_movie_list.visibility = View.GONE
                progress_bar_popular.visibility = View.GONE
            }
        }
    }


//при поворотах экрана, Activity будет пересоздаваться,
// а объект MainActivityViewModel будет спокойно себе жить в провайдере.
// И Activity после пересоздания сможет получить этот объект обратно
// и продолжить работу, как будто ничего не произошло.
//!!!!!!!!!!Модель жива, пока Activity не закроется окончательно.
private fun getViewModel(): MainActivityViewModel {
    //Создает ViewModelProvider, который будет создавать ViewModels через данную Factory
    //и сохранять их в хранилище данного ViewModelStoreOwner - this.
    return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        //Возвращаем существующую ViewModel или создаем новую
        //modelClass - Класс модели ViewModel для создания ее экземпляра, если он отсутствует
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            //возвращаем ViewModel, кот. явл. экземпляром данного типа T.
            return MainActivityViewModel(movieRepository) as T
        }
    })[MainActivityViewModel::class.java]  //ссылка на класс
}

}
