package com.example.filmfilter.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val inicioSesionUp = findViewById<Button>(R.id.btnIniciarSesionUpReg)
        val inicioSesionDown = findViewById<Button>(R.id.btnIniciarSesionDownReg)

        inicioSesionUp.setOnClickListener{
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
        }

        inicioSesionDown.setOnClickListener{
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
        }
    }


}