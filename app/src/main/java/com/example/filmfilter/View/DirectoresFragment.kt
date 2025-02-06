package com.example.filmfilter.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.filmfilter.R

class DirectoresFragment : Fragment() {

    private val directores = listOf(
        "Quentin Tarantino",
        "Martin Scorsese",
        "Steven Spielberg",
        "Christopher Nolan",
        "Greta Gerwig"
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

        val titulo = view.findViewById<TextView>(R.id.tvTituloPreferencias)
        titulo.text = "Elige tus directores favoritos"

        val listView = view.findViewById<ListView>(R.id.listViewPreferencias)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, directores)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val sharedPreferences = requireActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val savedSelections = sharedPreferences.getStringSet("Directores", emptySet()) ?: emptySet()

        // Restaurar selecciones previas
        for (i in directores.indices) {
            if (savedSelections.contains(directores[i])) {
                listView.setItemChecked(i, true)
            }
        }

        // Guardar selección cuando el usuario elige una opción
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItems = mutableSetOf<String>()
            for (i in 0 until listView.count) {
                if (listView.isItemChecked(i)) {
                    selectedItems.add(directores[i])
                }
            }
            sharedPreferences.edit().putStringSet("Directores", selectedItems).apply()
        }
    }
}
