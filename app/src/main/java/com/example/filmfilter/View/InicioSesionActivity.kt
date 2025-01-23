package com.example.filmfilter.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R

class InicioSesionActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicar_sesion)

        val crearCuentaDown = findViewById<Button>(R.id.btnCrearSesionDownIS)
        val crearCuentaUp = findViewById<Button>(R.id.btnCrearSesionUpIS)
        val olvidePassword = findViewById<Button>(R.id.btnOlvidarPasswordIS)

        crearCuentaUp.setOnClickListener{
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        crearCuentaDown.setOnClickListener{
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        olvidePassword.setOnClickListener{
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }
    }
}