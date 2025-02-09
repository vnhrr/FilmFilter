package com.example.filmfilter.View

import ApiClient
import com.example.filmfilter.Model.MovieDetailsResponse
import ApiService
import android.content.SharedPreferences
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.filmfilter.R
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetallesPeliActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_peli)

        val volver = findViewById<Button>(R.id.btBack)
        val fav = findViewById<Button>(R.id.btFav)


        sharedPreferences = getSharedPreferences("Favoritos", Context.MODE_PRIVATE)

        // Obtener los datos de la película desde el intent
        val movieId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title") ?: "Título desconocido"
        val posterPath = intent.getStringExtra("posterPath") ?: ""
        val genreIdsString = intent.getStringExtra("genre") ?: "No disponible"
        val releaseYear = intent.getStringExtra("releaseYear") ?: "No disponible"
        val synopsis = intent.getStringExtra("synopsis") ?: "No disponible"

        // Asignar los datos a las vistas
        val movieTitle = findViewById<TextView>(R.id.movieTitle)
        val moviePoster = findViewById<ImageView>(R.id.moviePoster)
        val movieGenre = findViewById<TextView>(R.id.movieGenre)
        val movieYear = findViewById<TextView>(R.id.movieYear)
        val movieSynopsis = findViewById<TextView>(R.id.movieSynopsis)

        movieTitle.text = title
        movieGenre.text = "Género: $genreIdsString"
        movieYear.text = "Año de lanzamiento: $releaseYear"
        movieSynopsis.text = synopsis

        // Cargar la imagen con Glide
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/$posterPath").into(moviePoster)

        volver.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }

        fav.setOnClickListener {
            guardarEnFavoritos(title)
        }
    }

    private fun guardarEnFavoritos(title: String) {
        val sharedPreferences = getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Obtener la lista actual de favoritos
        val favoritosJson = sharedPreferences.getString("favoritos", "[]") ?: "[]"
        val favoritosArray = JSONArray(favoritosJson)


        // Verificar si ya existe
        for (i in 0 until favoritosArray.length()) {
            if (favoritosArray.getString(i) == title) {
                Toast.makeText(this, "Esta película ya está en favoritos", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Agregar el nuevo título
        favoritosArray.put(title)

        // Guardar la lista actualizada en SharedPreferences
        editor.putString("favoritos", favoritosArray.toString())
        editor.apply()

        println("✅ PELÍCULA GUARDADA EN FAVORITOS: $favoritosArray")
        Toast.makeText(this, "Película añadida a favoritos", Toast.LENGTH_SHORT).show()
    }
}
