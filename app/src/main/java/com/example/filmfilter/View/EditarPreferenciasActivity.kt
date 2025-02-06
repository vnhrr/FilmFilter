package com.example.filmfilter.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R

class EditarPreferenciasActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_preferencias)

        listView = findViewById(R.id.listViewPreferenciasEditar)
        saveButton = findViewById(R.id.btnGuardarPreferencias)

        val sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        val categorias = listOf("Actores", "Directores", "Géneros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorias)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val categoriaSeleccionada = categorias[position]
            mostrarOpcionesDeCategoria(categoriaSeleccionada, sharedPreferences)
        }

        saveButton.setOnClickListener {
            Toast.makeText(this, "Preferencias guardadas correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun mostrarOpcionesDeCategoria(categoria: String, sharedPreferences: SharedPreferences) {
        val opciones = when (categoria) {
            "Actores" -> listOf("Tom Hardy", "Harrison Ford", "Charlize Theron", "Ryan Gosling", "Margot Robbie")
            "Directores" -> listOf("Quentin Tarantino", "Martin Scorsese", "Steven Spielberg", "Christopher Nolan", "Greta Gerwig")
            "Géneros" -> listOf("Comedia", "Drama", "Acción", "Ciencia ficción", "Terror")
            else -> emptyList()
        }

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Selecciona tus preferencias")
        val selectedItems = sharedPreferences.getStringSet(categoria, emptySet())?.toMutableSet() ?: mutableSetOf()
        val checkedItems = opciones.map { selectedItems.contains(it) }.toBooleanArray()

        dialog.setMultiChoiceItems(opciones.toTypedArray(), checkedItems) { _, which, isChecked ->
            if (isChecked) {
                selectedItems.add(opciones[which])
            } else {
                selectedItems.remove(opciones[which])
            }
        }

        dialog.setPositiveButton("Guardar") { _, _ ->
            sharedPreferences.edit().putStringSet(categoria, selectedItems).apply()
        }

        dialog.setNegativeButton("Cancelar", null)
        dialog.show()
    }
}
