package com.example.filmfilter.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val volver = findViewById<Button>(R.id.btnVolverPass)

        volver.setOnClickListener{
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
        }


    }
}