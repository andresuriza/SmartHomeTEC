package com.example.samrthometec

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityRegistroBinding

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el binding para el layout de registro
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referencias a los elementos de la interfaz
        val registerUsername: EditText = binding.registerUsername
        val registerPassword: EditText = binding.registerPassword
        val registerConfirmButton: Button = binding.registerConfirmButton

        // Configuración del botón de registro
        registerConfirmButton.setOnClickListener {
            val username = registerUsername.text.toString()
            val password = registerPassword.text.toString()

            // Aquí puedes agregar la lógica para registrar el usuario
            // Mostrar mensaje de éxito (ficticio)
            Toast.makeText(this, "User $username registered!", Toast.LENGTH_SHORT).show()

            // Opcionalmente, podrías regresar al login después del registro
            finish()
        }
    }
}
