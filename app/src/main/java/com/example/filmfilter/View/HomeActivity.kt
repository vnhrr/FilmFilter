package com.example.filmfilter.View

import ApiService
import Movie
import MovieResponse
import PersonResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmfilter.Model.GenreResponse
import com.example.filmfilter.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var movieAdapter1: PruebaAdapter
    private lateinit var movieAdapter2: PruebaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val search = findViewById<ImageButton>(R.id.imageButtonSearch)
        search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val filtro = findViewById<Spinner>(R.id.spinnerFiltro)
        val opciones = listOf("Actores", "Directores", "Géneros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filtro.adapter = adapter

        // Detectar cuando el usuario selecciona una opción
        filtro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val opcionSeleccionada = opciones[position]

                when (opcionSeleccionada) {
                    "Actores" -> {
                        val actores = listOf("Will Smith") // Puedes cambiar esto para ingresar nombres dinámicos
                        fetchMoviesByActors(actores)
                    }
                    "Directores" -> {
                        val directores = listOf("Quentin Tarantino") // Cambia según necesidad
                        fetchMoviesByDirectors(directores)
                    }
                    "Géneros" -> {
                        val generos = listOf("Acción") // Cambia según necesidad
                        fetchMoviesByGenres(generos)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona nada
            }
        }

        // Inicializar RecyclerViews
        recyclerView1 = findViewById(R.id.recyclerView1)
        recyclerView2 = findViewById(R.id.recyclerView2)

        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        movieAdapter1 = PruebaAdapter(emptyList())
        movieAdapter2 = PruebaAdapter(emptyList())

        recyclerView1.adapter = movieAdapter1
        recyclerView2.adapter = movieAdapter2

        val tit_list1 = findViewById<TextView>(R.id.tit_primeraLista)
        tit_list1.text = "Recomendaciones"

        fetchRecommendedMovies()
    }

    //----------------------------------------------------------------------------------------------
    // CARGAR PELÍCULAS RECOMENDADAS (Ejemplo de "Para Ti")
    //----------------------------------------------------------------------------------------------
    private fun fetchRecommendedMovies() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getPopularMovies(apiKey = "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()
                        movieAdapter1.updateMovies(movies) // Cargar en el primer RecyclerView
                    } else {
                        Toast.makeText(this@HomeActivity, "Error al obtener películas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Error al obtener películas recomendadas: ${t.message}")
                }
            })
    }

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELÍCULAS POR ACTORES
    //----------------------------------------------------------------------------------------------
    private fun fetchMoviesByActors(actorNames: List<String>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val actorIds = mutableListOf<Int>()

        actorNames.forEach { actorName ->
            apiService.searchPerson(apiKey = "aeb34317761b06ce97f327fe28f338b0", actorName)
                .enqueue(object : Callback<PersonResponse> {
                    override fun onResponse(call: Call<PersonResponse>, response: Response<PersonResponse>) {
                        if (response.isSuccessful) {
                            val person = response.body()?.results?.firstOrNull()
                            person?.let {
                                actorIds.add(it.id)

                                if (actorIds.size == actorNames.size) {
                                    getMoviesByActors(actorIds)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PersonResponse>, t: Throwable) {
                        Log.e("API_ERROR", "Error al buscar actor: ${t.message}")
                    }
                })
        }
    }

    private fun getMoviesByActors(actorIds: List<Int>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val allMovies = mutableListOf<Movie>()

        actorIds.forEach { actorId ->
            apiService.getMoviesByActors(apiKey = "aeb34317761b06ce97f327fe28f338b0", actorIds = actorId.toString())
                .enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.results?.let { movies ->
                                allMovies.addAll(movies)
                            }

                            if (allMovies.isNotEmpty()) {
                                movieAdapter2.updateMovies(allMovies.distinctBy { it.id }) // Se muestra en el segundo RecyclerView
                            }
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        Log.e("API_ERROR", "Error al obtener películas del actor $actorId: ${t.message}")
                    }
                })
        }
    }

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELÍCULAS POR DIRECTORES
    //----------------------------------------------------------------------------------------------
    private fun fetchMoviesByDirectors(directorNames: List<String>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val directorIds = mutableListOf<Int>()

        directorNames.forEach { directorName ->
            apiService.searchPerson(apiKey = "aeb34317761b06ce97f327fe28f338b0", query = directorName)
                .enqueue(object : Callback<PersonResponse> {
                    override fun onResponse(call: Call<PersonResponse>, response: Response<PersonResponse>) {
                        if (response.isSuccessful) {
                            val person = response.body()?.results?.firstOrNull()
                            person?.let { directorIds.add(it.id) }

                            if (directorIds.size == directorNames.size) {
                                getMoviesByDirectors(directorIds)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PersonResponse>, t: Throwable) {
                        Log.e("API_ERROR", "Error al buscar director: ${t.message}")
                    }
                })
        }
    }

    private fun getMoviesByDirectors(directorIds: List<Int>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val allMovies = mutableListOf<Movie>()

        directorIds.forEach { directorId ->
            apiService.getMoviesByDirectors(apiKey = "aeb34317761b06ce97f327fe28f338b0", directorsIds = directorId.toString())
                .enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.results?.let { movies ->
                                allMovies.addAll(movies)
                            }

                            if (allMovies.isNotEmpty()) {
                                movieAdapter2.updateMovies(allMovies.distinctBy { it.id }) // Se muestran en el segundo RecyclerView
                            }
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        Log.e("API_ERROR", "Error al obtener películas del director $directorId: ${t.message}")
                    }
                })
        }
    }

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELÍCULAS POR GENEROS
    //----------------------------------------------------------------------------------------------
    private fun fetchMoviesByGenres(genreNames: List<String>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val genreIds = mutableListOf<Int>()

        // Obtener la lista de géneros disponibles
        apiService.getGenres(apiKey = "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<GenreResponse> {
                override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                    if (response.isSuccessful) {
                        val genres = response.body()?.genres

                        // Buscar los IDs de los géneros solicitados
                        genreNames.forEach { genreName ->
                            genres?.find { it.name.equals(genreName, ignoreCase = true) }?.let {
                                genreIds.add(it.id)
                            }
                        }

                        // Si encontramos géneros, hacemos la consulta
                        if (genreIds.isNotEmpty()) {
                            getMoviesByGenres(genreIds)
                        } else {
                            Toast.makeText(this@HomeActivity, "No se encontraron géneros", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Error al obtener géneros: ${t.message}")
                }
            })
    }

    // Obtener películas por género
    private fun getMoviesByGenres(genreIds: List<Int>) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        val idsString = genreIds.joinToString(",") // Convertir la lista de IDs en una cadena separada por comas

        apiService.getMoviesByGenres(apiKey = "aeb34317761b06ce97f327fe28f338b0", genreIds = idsString)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()

                        // Asegurar que las películas incluyen posterPath
                        movieAdapter2.updateMovies(movies)
                    } else {
                        Toast.makeText(this@HomeActivity, "Error al obtener películas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Error al obtener películas por género: ${t.message}")
                }
            })
    }

}
