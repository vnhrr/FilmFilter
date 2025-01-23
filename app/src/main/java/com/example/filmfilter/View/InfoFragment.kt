package com.example.filmfilter.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.filmfilter.R

class InfoFragment : Fragment() {
    // Este método se llama para que el fragmento cree su vista de usuario.
    // Aquí definimos la interfaz gráfica del fragmento.
    override fun onCreateView(
        inflater: LayoutInflater, // Un objeto LayoutInflater para inflar las vistas del fragmento.
        container: ViewGroup?,    // El contenedor donde se insertará el fragmento, puede ser null.
        savedInstanceState: Bundle? // Un Bundle que contiene datos previamente guardados, se suele usar para restaurar estados.
    ): View? {
        // Inflamos la vista del fragmento utilizando el layout 'fragment_ejemplo'.
        // 'R.layout.fragment_ejemplo' se refiere al archivo XML que define la UI del fragmento.
        // 'container' es donde se insertará la vista del fragmento.
        // 'false' indica que no debemos adjuntar la vista inflada al contenedor de forma inmediata.
        // Este inflado convierte el layout XML en objetos de vista reales.
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    // Este método se llama después de que la vista del fragmento haya sido creada.
    // Aquí puedes realizar tareas adicionales relacionadas con la inicialización de la vista.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Llamada al método de la superclase.
        // Aquí puedes inicializar elementos de tu UI, por ejemplo, un TextView.
        // 'view' es la vista raíz de tu fragmento.
    }
}