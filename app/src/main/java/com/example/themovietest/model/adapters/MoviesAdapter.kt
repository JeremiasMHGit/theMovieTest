package com.example.themovietest.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.themovietest.R
import com.example.themovietest.databinding.ItemListMovieBinding
import com.example.themovietest.model.entity.Movie
import com.example.themovietest.utils.GetImageURL
import kotlinx.android.synthetic.main.item_list_movie.view.*
import kotlin.properties.Delegates

class MoviesAdapter : RecyclerView.Adapter<MoviesViewHolder>() {

    private var movies: List<Movie> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }
    fun setMoviesList(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val rootView = ItemListMovieBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}

class MoviesViewHolder(private val itemSearch: ItemListMovieBinding) :
    RecyclerView.ViewHolder(itemSearch.root) {
    fun bind(movie: Movie) {
        itemView.run {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

            tv_movieName.text = movie.title.toString()
            tv_productionDate.text = movie.release_date.toString()
            tv_rating.text = movie.vote_average.toString()

            Glide.with(this).load(GetImageURL.getImageFullURL(movie.poster_path.toString()))
                .fitCenter()
                .apply(requestOptions)
                .placeholder(R.drawable.ic_baseline_image)
                .into(iv_moviePoster)
        }
    }

}


/*var listFiltered = arrayListOf<Movie>()
    class MoviesViewHolder(private val itemSearch: ItemListMovieBinding) :
        RecyclerView.ViewHolder(itemSearch.root) {
        fun bind(data: Movie) {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

            itemSearch.tvMovieName.text = data.title.toString()
            itemSearch.tvProductionDate.text = data.release_date.toString()
            itemSearch.tvRating.text = data.vote_average.toString()

            itemView.run {
                Glide.with(this).load(GetImageURL.getImageFullURL(data.poster_path.toString()))
                    .fitCenter()
                    .apply(requestOptions)
                    .placeholder(R.drawable.ic_baseline_image)
                    .into(itemSearch.ivMoviePoster)

                setOnClickListener {
                onMovieClickListener?.onItemClicked(movie)
            }
            }
        }
    }*/