package com.example.filmfilter.View

import Movie
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmfilter.R

class PruebaAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<PruebaAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movieTitle)
        val poster: ImageView = view.findViewById(R.id.moviePoster)

        fun bind(movie: Movie) {
            title.text = movie.title

            // Cargar la imagen con Glide
            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.estilo_et_busqueda) // Imagen de carga
                .error(R.drawable.estilo_et_busqueda) // Imagen en caso de error
                .into(poster)

            // Agregar el evento de clic para abrir la actividad de detalles
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetallesPeliActivity::class.java).apply {
                    putExtra("title", movie.title)
                    putExtra("posterPath", movie.poster_path)
                    putExtra("genre", movie.genre_ids?.joinToString(", ") ?: "No disponible")
                    putExtra("releaseYear", movie.release_date?.split("-")?.get(0) ?: "No disponible")
                    putExtra("director", "No disponible") // Debes obtenerlo desde la API
                    putExtra("actors", "No disponible") // Debes obtenerlo desde la API
                    putExtra("synopsis", movie.overview ?: "No disponible")
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.formato_home, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
