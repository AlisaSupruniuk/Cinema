package by.supruniuk.alisa.cinema.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "ad16c8e0e0197a82b75db19a22aa0578"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342" //    /7D430eqZj8y3oVkLFfsWXGRcpEG.jpg

object TheMovieDBClient {

    fun getClient(): TheMovieDBInterface{

         val requestInterceptor = Interceptor{ chain ->

            val newUrl = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
         }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

        return  Retrofit.Builder() //собирается объект, в кот. устанавливается
            .client(okHttpClient) //клиент - это то как библиотека будет работать с сетью
            .baseUrl(BASE_URL) //адрес относительно которого будут идти запросы
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) //корутина
            .addConverterFactory(GsonConverterFactory.create())//с помощью этого метода добавляется (фабрика)конвертер(собирать/ разбирать объекты, кот.приходят с сервера/
            .build() //создается объект со всеми настройками
            .create(TheMovieDBInterface::class.java)  //создается экземпляр класса которому делали все аннотации для работы с сетью
    }
}

