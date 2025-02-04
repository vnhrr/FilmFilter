package com.example.filmfilter.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.filmfilter.R

class GenerosFragment : Fragment() {

    private val generos = listOf(
        "Comedia",
        "Drama",
        "Acción",
        "Ciencia ficción",
        "Terror"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cambiar el texto del título
        val titulo = view.findViewById<TextView>(R.id.tvTituloPreferencias)
        titulo.text = "Elige tus géneros favoritos"

        // Configurar el ListView con los géneros
        val listView = view.findViewById<ListView>(R.id.listViewPreferencias)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, generos)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }
}
