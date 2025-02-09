package com.example.filmfilter.View

import ApiService
import Movie
import MovieResponse
import PersonResponse
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
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

        val fav = findViewById<Button>(R.id.buttonVerFav)
        fav.setOnClickListener {
            val intent = Intent(this, FavoritosActivity::class.java)
            startActivity(intent)
        }

        val filtro = findViewById<Spinner>(R.id.spinnerFiltro)
        val opciones = listOf("Actores", "Directores", "Géneros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filtro.adapter = adapter

        val sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE)

        filtro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val opcionSeleccionada = opciones[position]

                when (opcionSeleccionada) {
                    "Actores" -> {
                        val actores = sharedPreferences.getStringSet("Actores", emptySet())?.toList() ?: emptyList()
                        fetchMoviesByActors(actores)
                    }
                    "Directores" -> {
                        val directores = sharedPreferences.getStringSet("Directores", emptySet())?.toList() ?: emptyList()
                        fetchMoviesByDirectors(directores)
                    }
                    "Géneros" -> {
                        // Recuperar los géneros en el momento en que se selecciona el filtro
                        val generos = sharedPreferences.getStringSet("Géneros", emptySet())?.toList() ?: emptyList()
                        Log.d("DEBUG_RECUPERAR", "Géneros recuperados al cambiar filtro en HomeActivity: $generos")

                        if (generos.isNotEmpty()) {
                            fetchMoviesByGenres(generos)
                        } else {
                            Toast.makeText(this@HomeActivity, "No hay géneros seleccionados", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val editPreferencesButton = findViewById<ImageButton>(R.id.imageButtonProfile)
        editPreferencesButton.setOnClickListener {
            val intent = Intent(this, EditarPreferenciasActivity::class.java)
            startActivity(intent)
        }

        val handler = android.os.Handler()
        handler.postDelayed({
            val sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
            val generosGuardados = sharedPreferences.getStringSet("Géneros", emptySet()) ?: emptySet()

            Log.d("DEBUG_RECUPERAR_DELAY", "Géneros recuperados tras delay en HomeActivity: $generosGuardados")

            if (generosGuardados.isNotEmpty()) {
                fetchMoviesByGenres(generosGuardados.toList())
            } else {
                Toast.makeText(this, "No hay géneros seleccionados", Toast.LENGTH_SHORT).show()
            }
        }, 500) // Esperar 500ms antes de leer los datos


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

        // Mapeo manual de nombres incorrectos a nombres correctos
        val genreFixMap = mapOf(
            "Acción" to "Acción",
            "Ciencia ficción" to "Ciencia ficción",
            "Comedia" to "Comedia",  // No necesita conversión
            "Drama" to "Drama",      // No necesita conversión
            "Terror" to "Terror"     // No necesita conversión
        )

        val correctedGenreNames = genreNames.map { genreFixMap[it] ?: it }

        Log.d("DEBUG_GENRES", "Géneros seleccionados para API: $genreNames")

        apiService.getGenres(apiKey = "aeb34317761b06ce97f327fe28f338b0")
            .enqueue(object : Callback<GenreResponse> {
                override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                    if (response.isSuccessful) {
                        val genres = response.body()?.genres
                        val genreIds = mutableListOf<Int>()

                        Log.d("DEBUG_API_GENRES", "Géneros obtenidos de la API: ${genres?.map { it.name }}")

                        // Ajustar nombres incorrectos antes de la comparación
                        genres?.forEach { genre ->
                            val fixedName = genreFixMap[genre.name] ?: genre.name
                            if (genreNames.contains(fixedName)) {
                                genreIds.add(genre.id)
                            }
                        }

                        Log.d("DEBUG_GEN_IDS", "IDs de géneros encontrados: $genreIds")

                        if (genreIds.isNotEmpty()) {
                            getMoviesByGenres(genreIds)
                        } else {
                            Toast.makeText(this@HomeActivity, "No se encontraron géneros", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ERROR_API", "Error en la respuesta de la API al obtener géneros")
                    }
                }

                override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                    Log.e("ERROR_API", "Fallo en la API al obtener géneros: ${t.message}")
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
