package com.example.filmfilter.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmfilter.R
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Conectar los campos con la interfaz XML
        emailEditText = findViewById(R.id.etEmailRegistro)
        passwordEditText = findViewById(R.id.etPaswword1Registro)
        confirmPasswordEditText = findViewById(R.id.etPaswword2Registro)
        registerButton = findViewById(R.id.btnRegistrarseReg)

        // Botones para ir a Inicio de Sesión
        val inicioSesionUp = findViewById<Button>(R.id.btnIniciarSesionUpReg)
        val inicioSesionDown = findViewById<Button>(R.id.btnIniciarSesionDownReg)

        inicioSesionUp.setOnClickListener {
            startActivity(Intent(this, InicioSesionActivity::class.java))
        }

        inicioSesionDown.setOnClickListener {
            startActivity(Intent(this, InicioSesionActivity::class.java))
        }

        // Botón de registro con Firebase
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar que las contraseñas coincidan
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar al usuario en Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Ir a la pantalla de preferencias
                    startActivity(Intent(this, PreferenciasActivity::class.java))
                    finish() // Cierra la actividad de registro
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
