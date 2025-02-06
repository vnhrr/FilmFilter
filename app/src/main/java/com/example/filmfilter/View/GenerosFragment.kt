package com.example.filmfilter.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.filmfilter.R

class GenerosFragment : Fragment() {

    private val generos = listOf("Comedia", "Drama", "Acción", "Ciencia ficción", "Terror")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titulo = view.findViewById<TextView>(R.id.tvTituloPreferencias)
        titulo.text = "Elige tus géneros favoritos"

        val listView = view.findViewById<ListView>(R.id.listViewPreferencias)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, generos)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val sharedPreferences = requireActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val savedSelections = sharedPreferences.getStringSet("Géneros", emptySet()) ?: emptySet()

        // Restaurar selecciones previas
        for (i in generos.indices) {
            if (savedSelections.contains(generos[i])) {
                listView.setItemChecked(i, true)
            }
        }

        // Guardar selección cuando el usuario elige una opción
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItems = mutableSetOf<String>()

            for (i in 0 until listView.count) {
                if (listView.isItemChecked(i)) {
                    selectedItems.add(generos[i])
                }
            }

            val sharedPreferences = requireActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
            sharedPreferences.edit().putStringSet("Géneros", selectedItems).commit() // Forzar escritura inmediata

            // Verificar que los datos se guardaron correctamente
            val savedGenres = sharedPreferences.getStringSet("Géneros", emptySet()) ?: emptySet()
            Log.d("DEBUG_PREF", "Géneros guardados en SharedPreferences desde GenerosFragment: $savedGenres")
        }
    }
}
