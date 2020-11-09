package by.supruniuk.alisa.cinema.data.repository

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.supruniuk.alisa.cinema.R
import by.supruniuk.alisa.cinema.data.api.POSTER_BASE_URL
import by.supruniuk.alisa.cinema.data.model.Movie
import by.supruniuk.alisa.cinema.ui.single_movie_details.SingleMovie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_list_item.view.*


class MovieAdapter(private val context: Context): RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder>(){

    private var data = mutableListOf<Movie?>()

    fun updateData(newData: List<Movie?>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    //создание держателя представления
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_list_item,
            parent,
            false
        )
        return MovieItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(data[position], context)
    }



    class MovieItemViewHolder (view : View) : RecyclerView.ViewHolder(view){ //ViewHolder описывает представление элемента и метаданные о его месте в RecyclerView.
        fun bind(movie: Movie?, context: Context){
            itemView.cv_tv_movie_title.text = movie?.title
            itemView.cv_tv_release_date.text = movie?.releaseDate

            val moviePosterURL: String = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster)


            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = data.size
}