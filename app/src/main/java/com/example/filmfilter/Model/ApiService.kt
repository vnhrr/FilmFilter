import com.example.filmfilter.Model.GenreResponse
import com.example.filmfilter.Model.MovieDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELICULAS POR DIRECTOR
    //----------------------------------------------------------------------------------------------
    // Buscar el ID de un director por nombre
    @GET("search/person")
    fun searchPerson(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<PersonResponse>

    // Obtener películas dirigidas por el director (filtrando con `with_crew`)
    @GET("discover/movie")
    fun getMoviesByDirectors(
        @Query("api_key") apiKey: String,
        @Query("with_crew") directorsIds: String
    ): Call<MovieResponse>
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    // BUSCAR PELICULAS POR ACTOR
    //----------------------------------------------------------------------------------------------
    // Burcar id del actor por su nombre
    @GET("search/person")
    fun searchActor(
        @Query("api_key") apiKey: String,
        @Query("query") actorName: String
    ): Call<PersonResponse>

    // Obtener películas de un actor por ID
    @GET("discover/movie")
    fun getMoviesByActors(
        @Query("api_key") apiKey: String,
        @Query("with_cast") actorIds: String
    ): Call<MovieResponse>
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELICULAS POR GENERO
    //----------------------------------------------------------------------------------------------
    // Burcar id del genero por su nombre
    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Call<GenreResponse>

    @GET("discover/movie")
    fun getMoviesByGenres(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreIds: String
    ): Call<MovieResponse>

    //----------------------------------------------------------------------------------------------
    // OBTENER DETALLES DE LA PELICULA
    //----------------------------------------------------------------------------------------------
    @GET("movie/{movie_id}?append_to_response=credits")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailsResponse>

    //----------------------------------------------------------------------------------------------
    // BUSCAR PELICULAS POR NOMBRE
    //----------------------------------------------------------------------------------------------
    interface ApiService {
        @GET("search/movie")
        fun searchMovie(
            @Query("query") query: String
        ): Call<MovieResponse>
    }

    @GET("search/movie")
    fun searchMovieByTitle(
        @Query("query") title: String,
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>


}

