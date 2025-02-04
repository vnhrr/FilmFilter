package com.example.filmfilter.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R
import com.google.firebase.auth.FirebaseAuth

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicar_sesion)

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Conectar con elementos de la UI
        val iniSesion = findViewById<Button>(R.id.btnIniciarSesionIS)
        val crearCuentaDown = findViewById<Button>(R.id.btnCrearSesionDownIS)
        val crearCuentaUp = findViewById<Button>(R.id.btnCrearSesionUpIS)
        val olvidePassword = findViewById<Button>(R.id.btnOlvidarPasswordIS)

        emailEditText = findViewById(R.id.etCorreoInicioSesion) // Agrega este ID en tu XML
        passwordEditText = findViewById(R.id.etPasswordInicioSesion) // Agrega este ID en tu XML

        // Navegar a pantalla de registro
        crearCuentaUp.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        crearCuentaDown.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Navegar a pantalla de recuperación de contraseña
        olvidePassword.setOnClickListener {
            startActivity(Intent(this, PasswordActivity::class.java))
        }

        // Iniciar sesión en Firebase
        iniSesion.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish() // Cierra la actividad de inicio de sesión
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
