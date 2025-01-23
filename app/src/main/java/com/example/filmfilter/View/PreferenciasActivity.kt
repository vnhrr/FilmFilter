package com.example.filmfilter.View

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R

class PreferenciasActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferencias)

        if (savedInstanceState == null) { // Comenzamos una transacción de fragmentos con el administrador de fragmentos (FragmentManager).
            supportFragmentManager.beginTransaction() // EjemploFragment() crea una nueva instancia de nuestro fragmento personalizado.
                .replace(R.id.containerFragmentInfo_Lista, InfoFragment())
                .commit()
        }

        val volver = findViewById<Button>(R.id.btnVolverPref)
        val omitir = findViewById<Button>(R.id.btnOmitirPref)
        val siguiente = findViewById<Button>(R.id.btnSiguiente)

        val num1 = findViewById<TextView>(R.id.tv1)
        val num2 = findViewById<TextView>(R.id.tv2)
        val num3 = findViewById<TextView>(R.id.tv3)
        val num4 = findViewById<TextView>(R.id.tv4)

        val colorActual = Color.parseColor("#00D7E6")
        val colorInactivo = Color.parseColor("#D3EFF8")


        volver.isEnabled = false
        volver.visibility = View.INVISIBLE

        volver.setOnClickListener {
            when {
                (num4.background as? ColorDrawable)?.color == colorActual -> {
                    num4.setBackgroundColor(Color.WHITE)
                    num3.setBackgroundColor(colorActual)
                }

                (num3.background as? ColorDrawable)?.color == colorActual -> {
                    num3.setBackgroundColor(Color.WHITE)
                    num2.setBackgroundColor(colorActual)
                }

                (num2.background as? ColorDrawable)?.color == colorActual -> {
                    num2.setBackgroundColor(Color.WHITE)
                    num1.setBackgroundColor(colorActual)
                    volver.isEnabled = false
                    volver.visibility = View.INVISIBLE
                }
                else -> {

                }
            }
        }

        omitir.setOnClickListener {
            volver.isEnabled = true
            volver.visibility = View.VISIBLE

            when {
                (num1.background as? ColorDrawable)?.color == colorActual -> {
                    num1.setBackgroundColor(colorInactivo)
                    num2.setBackgroundColor(colorActual)
                }

                (num2.background as? ColorDrawable)?.color == colorActual -> {
                    num2.setBackgroundColor(colorInactivo)
                    num3.setBackgroundColor(colorActual)
                }

                (num3.background as? ColorDrawable)?.color == colorActual -> {
                    num3.setBackgroundColor(colorInactivo)
                    num4.setBackgroundColor(colorActual)
                }

                (num4.background as? ColorDrawable)?.color == colorActual -> {
                    num4.setBackgroundColor(colorInactivo)
                }

                else -> {

                }
            }
        }
        siguiente.setOnClickListener {
            volver.isEnabled = true
            volver.visibility = View.VISIBLE

            when {
                (num1.background as? ColorDrawable)?.color == colorActual -> {
                    num1.setBackgroundColor(colorInactivo)
                    num1.setTextColor(Color.BLACK)
                    num2.setBackgroundColor(colorActual)
                }

                (num2.background as? ColorDrawable)?.color == colorActual -> {
                    num2.setBackgroundColor(colorInactivo)
                    num2.setTextColor(Color.BLACK)
                    num3.setBackgroundColor(colorActual)
                }

                (num3.background as? ColorDrawable)?.color == colorActual -> {
                    num3.setBackgroundColor(colorInactivo)
                    num3.setTextColor(Color.BLACK)
                    num4.setBackgroundColor(colorActual)
                }

                (num4.background as? ColorDrawable)?.color == colorActual -> {
                    num4.setBackgroundColor(colorInactivo)
                    num4.setTextColor(Color.BLACK)
                }

                else -> {

                }
            }
        }
    }
}

