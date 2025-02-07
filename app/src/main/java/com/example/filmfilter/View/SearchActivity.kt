package com.example.filmfilter.View

import Movie
import MovieResponse
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var etBusqueda: EditText
    private lateinit var listViewPelis: ListView
    private lateinit var adapter: ArrayAdapter<String>

    private val movieList = mutableListOf<Movie>() // Lista de objetos Movie
    private val apiService = ApiClient.retrofit.create(ApiService.ApiService::class.java) // Cliente API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etBusqueda = findViewById(R.id.etBusqueda)
        listViewPelis = findViewById(R.id.listViewPelis)
        val home = findViewById<ImageButton>(R.id.imageButtonHome)

        // Configurar adapter para mostrar nombres de películas
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listViewPelis.adapter = adapter

        home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Agregar listener al EditText para capturar cambios en la búsqueda
        etBusqueda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.length >= 3) {
                    searchMovie(query)
                } else {
                    movieList.clear()
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Agregar listener a ListView para abrir detalles
        listViewPelis.setOnItemClickListener { _, _, position, _ ->
            val selectedMovie = movieList[position]
            openMovieDetails(selectedMovie)
        }
    }

    // Función para buscar películas por nombre
    private fun searchMovie(query: String) {
        apiService.searchMovie(query = query).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieList.clear()
                    movieList.addAll(movies)

                    // Actualizar los nombres de las películas en el adapter
                    adapter.clear()
                    adapter.addAll(movies.map { it.title })
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@SearchActivity, "Error en la búsqueda", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Función para abrir la actividad de detalles
    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetallesPeliActivity::class.java).apply {
            putExtra("id", movie.id)
            putExtra("title", movie.title)
            putExtra("posterPath", movie.poster_path)
            putExtra("genre", movie.genre_ids?.joinToString(", ")) // Convertimos lista de géneros a String
            putExtra("releaseYear", movie.release_date?.substring(0, 4) ?: "No disponible")
            putExtra("synopsis", movie.overview)
        }
        startActivity(intent)
    }
}
