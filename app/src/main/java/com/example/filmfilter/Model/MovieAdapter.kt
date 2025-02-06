package com.example.filmfilter.View

import Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmfilter.R

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movieTitle)
        val poster: ImageView = view.findViewById(R.id.moviePoster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.formato_home, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title

        // Cargar la imagen con Glide
        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.estilo_et_busqueda) // Imagen de carga
            .error(R.drawable.estilo_et_busqueda) // Imagen en caso de error
            .into(holder.poster)
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}

