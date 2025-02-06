package com.example.filmfilter.View

import ApiClient
import com.example.filmfilter.Model.MovieDetailsResponse
import ApiService
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.filmfilter.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesPeliActivity : AppCompatActivity() {

    // Mapa de ID de géneros a nombres de género
    private val genreMap = mapOf(
        28 to "Acción", 12 to "Aventura", 16 to "Animación", 35 to "Comedia",
        80 to "Crimen", 99 to "Documental", 18 to "Drama", 10751 to "Familiar",
        14 to "Fantasía", 36 to "Historia", 27 to "Terror", 10402 to "Música",
        9648 to "Misterio", 10749 to "Romance", 878 to "Ciencia Ficción",
        10770 to "Película de TV", 53 to "Suspenso", 10752 to "Bélica", 37 to "Western"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_peli)

        val volver = findViewById<Button>(R.id.btBack)

        // Obtener los datos de la película desde el intent
        val movieId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val posterPath = intent.getStringExtra("posterPath")
        val genreIdsString = intent.getStringExtra("genre") ?: ""
        val releaseYear = intent.getStringExtra("releaseYear") ?: "No disponible"
        val synopsis = intent.getStringExtra("synopsis") ?: "No disponible"

        // Convertir IDs de géneros a nombres
        val genreNames = genreIdsString.split(", ").mapNotNull { it.toIntOrNull()?.let { id -> genreMap[id] } }
        val genreText = if (genreNames.isNotEmpty()) genreNames.joinToString(", ") else "No disponible"

        // Asignar los datos a las vistas
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val moviePoster = findViewById<ImageView>(R.id.moviePoster)
        val movieGenre = findViewById<TextView>(R.id.movieGenre)
        val movieYear = findViewById<TextView>(R.id.movieYear)
        val movieDirector = findViewById<TextView>(R.id.movieDirector)
        val movieActors = findViewById<TextView>(R.id.movieActors)
        val movieSynopsis = findViewById<TextView>(R.id.movieSynopsis)

        movieTitle.text = title
        movieGenre.text = "Género: $genreText"
        movieYear.text = "Año de lanzamiento: $releaseYear"
        movieSynopsis.text = synopsis

        // Cargar la imagen con Glide
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/$posterPath").into(moviePoster)

        // Obtener detalles adicionales como director y actores
        fetchMovieDetails(movieId, movieDirector, movieActors)

        volver.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }
    }

    private fun fetchMovieDetails(movieId: Int, directorView: TextView, actorsView: TextView) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getMovieDetails(movieId, apiKey = "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<MovieDetailsResponse> {
                override fun onResponse(call: Call<MovieDetailsResponse>, response: Response<MovieDetailsResponse>) {
                    if (response.isSuccessful) {
                        val movieDetails = response.body()
                        val director = movieDetails?.credits?.crew?.find { it.job == "Director" }?.name ?: "No disponible"
                        val actors = movieDetails?.credits?.cast?.take(5)?.joinToString(", ") { it.name } ?: "No disponible"

                        directorView.text = "Director: $director"
                        actorsView.text = "Actores: $actors"
                    }
                }

                override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                    directorView.text = "Director: No disponible"
                    actorsView.text = "Actores: No disponible"
                }
            })
    }
}
