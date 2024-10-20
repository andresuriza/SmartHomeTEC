package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)

        // Recibir el correo desde el Intent
        currentUser = intent.getStringExtra("username") ?: ""

        if (currentUser.isNotEmpty()) {
            loadUserInfo(currentUser)
        } else {
            Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad si no se recibe el usuario
        }

        // Configuración del botón "Siguiente"
        binding.nextButton.setOnClickListener {
            // Avanza a otra pantalla o realiza la acción deseada
            Toast.makeText(this, "Avanzando a la siguiente pantalla...", Toast.LENGTH_SHORT).show()
            intent.putExtra("username", currentUser)

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", currentUser)
            startActivity(intent)
        }
    }


    private fun loadUserInfo(currentUser: String) {
        // Obtener la información del usuario de la base de datos
        val user = dbManager.getUserInfo(currentUser)

        // Mostrar la información en los TextViews
        binding.correoValueTextView.text = user.correo
        binding.nombreValueTextView.text = user.nombre
        binding.apellido1ValueTextView.text = user.apellido1
        binding.apellido2ValueTextView.text = user.apellido2
        binding.regionValueTextView.text = user.region
    }
}
