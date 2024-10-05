package com.example.samrthometec

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el binding para el layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referencias a los elementos de la interfaz
        val usernameEditText: EditText = binding.username
        val passwordEditText: EditText = binding.password
        val loginButton: Button = binding.loginButton

        // Configuración del botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Lógica simple de validación de credenciales
            if (username == "admin" && password == "1234") {
                // Mostrar un mensaje de éxito
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                // Mostrar un mensaje de error
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
