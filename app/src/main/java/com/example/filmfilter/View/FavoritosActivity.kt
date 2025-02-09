package com.example.filmfilter.View

import ApiClient
import ApiService
import Movie
import MovieResponse
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritosActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: FavoritosAdapter
    private val listaFavoritos = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val volver = findViewById<Button>(R.id.btBack)
        volver.setOnClickListener{
            finish()
        }

        listView = findViewById(R.id.listViewFavoritos)

        // Inicializar adaptador con la función de eliminar favoritos y abrir detalles
        adapter = FavoritosAdapter(this, listaFavoritos,
            onRemoveFavorite = { pelicula -> eliminarDeFavoritos(pelicula.title) },
            onMovieClick = { title -> buscarDetallesDePelicula(title) }
        )

        listView.adapter = adapter

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val sharedPreferences = getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        val favoritosJson = sharedPreferences.getString("favoritos", "[]") ?: "[]"

        println("📂 Datos en SharedPreferences: $favoritosJson") // 🔥 Depuración

        try {
            val favoritosArray = JSONArray(favoritosJson)
            listaFavoritos.clear() // ✅ Limpiamos la lista antes de agregar nuevas películas

            if (favoritosArray.length() == 0) {
                println("⚠️ No hay películas guardadas en favoritos.")
                return
            }

            for (i in 0 until favoritosArray.length()) {
                val movieTitle = favoritosArray.getString(i)
                fetchMovieByTitle(movieTitle)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("❌ ERROR al cargar favoritos")
        }
    }



    /**
     * 🔍 Busca una película en la API por título y la añade a la lista.
     */
    private fun fetchMovieByTitle(title: String) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.searchMovieByTitle(title, "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        val movieList = response.body()?.results
                        if (!movieList.isNullOrEmpty()) {
                            val movie = movieList[0] // 🔥 Tomamos la primera coincidencia

                            runOnUiThread {
                                listaFavoritos.add(movie) // ✅ Agregar directamente a lista principal
                                adapter.notifyDataSetChanged() // ✅ Actualizar la UI inmediatamente
                            }

                            println("✅ Película añadida a favoritos: ${movie.title}")
                        } else {
                            println("⚠️ No se encontró la película: $title")
                        }
                    } else {
                        println("❌ API ERROR: No se pudo obtener detalles para la película: $title")
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    println("❌ Error en la API al obtener detalles de la película: $title")
                    t.printStackTrace()
                }
            })
    }




    /**
     * 🔍 Busca información detallada de una película antes de abrir `DetallesPeliActivity`.
     */
    private fun buscarDetallesDePelicula(title: String) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.searchMovieByTitle(title, "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        val movieList = response.body()?.results
                        if (!movieList.isNullOrEmpty()) {
                            val movie = movieList[0] // 🔥 Tomamos la primera coincidencia
                            abrirDetallesPelicula(movie)
                        } else {
                            println("⚠️ No se encontró información detallada para la película: $title")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    println("❌ Error en la API al obtener detalles de la película: $title")
                }
            })
    }

    /**
     * 🎬 Abre `DetallesPeliActivity` con la información completa de la película.
     */
    private fun abrirDetallesPelicula(movie: Movie) {
        val intent = Intent(this, DetallesPeliActivity::class.java).apply {
            putExtra("id", movie.id)
            putExtra("title", movie.title)
            putExtra("posterPath", movie.poster_path)
            putExtra("overview", movie.overview)
            putExtra("release_date", movie.release_date)
            putExtra("director", movie.director ?: "No disponible")
            putStringArrayListExtra("actors", ArrayList(movie.actors ?: listOf()))
        }

        startActivity(intent)
    }

    /**
     * 🗑️ Elimina una película de favoritos y actualiza la lista.
     */
    private fun eliminarDeFavoritos(title: String) {
        val sharedPreferences = getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val favoritosJson = sharedPreferences.getString("favoritos", "[]") ?: "[]"
        val favoritosArray = JSONArray(favoritosJson)
        val nuevosFavoritos = JSONArray()

        for (i in 0 until favoritosArray.length()) {
            val favTitle = favoritosArray.getString(i)
            if (favTitle != title) {
                nuevosFavoritos.put(favTitle)
            }
        }

        editor.putString("favoritos", nuevosFavoritos.toString())
        editor.apply()

        listaFavoritos.removeAll { it.title == title }
        adapter.notifyDataSetChanged()

        println("🗑️ Eliminada de favoritos: $title")
    }

    private fun getTotalFavoritos(): Int {
        val sharedPreferences = getSharedPreferences("Favoritos", Context.MODE_PRIVATE)
        val favoritosJson = sharedPreferences.getString("favoritos", "[]") ?: "[]"
        return JSONArray(favoritosJson).length()
    }
}
