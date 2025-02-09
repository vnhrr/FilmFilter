package com.example.filmfilter.View

import Movie
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.filmfilter.R

class FavoritosAdapter(
    private val context: Context,
    private val peliculas: MutableList<Movie>,
    private val onRemoveFavorite: (Movie) -> Unit,
    private val onMovieClick: (String) -> Unit // Callback para manejar clic en película
) : BaseAdapter() {

    override fun getCount(): Int = peliculas.size

    override fun getItem(position: Int): Any = peliculas[position]

    override fun getItemId(position: Int): Long = peliculas[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.linea_lista_pelis, parent, false)

        val titulo = view.findViewById<TextView>(R.id.tvTituloPeli)
        val imagen = view.findViewById<ImageView>(R.id.ivCartelPeli)
        val cbFav = view.findViewById<CheckBox>(R.id.cbFav)

        val pelicula = peliculas[position]

        titulo.text = pelicula.title

        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500/${pelicula.poster_path}")
            .placeholder(R.drawable.estilo_et_busqueda)
            .error(R.drawable.estilo_et_busqueda)
            .into(imagen)

        // Configurar evento de click en la película
        view.setOnClickListener {
            println("📌 Click en película: ${pelicula.title}")
            onMovieClick(pelicula.title)
        }

        cbFav.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                Toast.makeText(context, "${pelicula.title} eliminada de favoritos", Toast.LENGTH_SHORT).show()
                onRemoveFavorite(pelicula)
            }
        }

        return view
    }
}
