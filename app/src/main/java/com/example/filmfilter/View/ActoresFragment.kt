package com.example.filmfilter.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.filmfilter.R

class ActoresFragment : Fragment() {

    private val actores = listOf("Tom Hardy", "Harrison Ford", "Charlize Theron", "Ryan Gosling", "Margot Robbie")

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
        titulo.text = "Elige tus actores favoritos"

        val listView = view.findViewById<ListView>(R.id.listViewPreferencias)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, actores)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val sharedPreferences = requireActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val savedSelections = sharedPreferences.getStringSet("Actores", emptySet()) ?: emptySet()

        // Restaurar selecciones previas
        for (i in actores.indices) {
            if (savedSelections.contains(actores[i])) {
                listView.setItemChecked(i, true)
            }
        }

        // Guardar selección cuando el usuario elige una opción
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItems = mutableSetOf<String>()
            for (i in 0 until listView.count) {
                if (listView.isItemChecked(i)) {
                    selectedItems.add(actores[i])
                }
            }
            sharedPreferences.edit().putStringSet("Actores", selectedItems).apply()
        }
    }
}
