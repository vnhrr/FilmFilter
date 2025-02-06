data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String?,  // Fecha de lanzamiento (YYYY-MM-DD)
    val genre_ids: List<Int>?,  // Lista de IDs de los géneros
    val director: String?,      // Director (se obtiene en otra consulta)
    val actors: List<String>?   // Lista de actores (se obtiene en otra consulta)
)
