package com.example.samrthometec

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityAposentosBinding
import com.example.samrthometec.DatabaseManager

class Aposentos : AppCompatActivity() {

    // Lista para almacenar los aposentos del usuario
    private val aposentosList = mutableListOf<String>()
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String // Almacena el usuario actual

    // Variable de View Binding
    private lateinit var binding: ActivityAposentosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar View Binding
        binding = ActivityAposentosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el DatabaseManager
        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: "" // Obtener el usuario actual
        loadAposentos()

        // Configuración del botón para agregar aposentos
        binding.addAposentoButton.setOnClickListener {
            val aposentoName = binding.aposentoEditText.text.toString().trim()

            // Verificar si el nombre del aposento no está vacío
            if (aposentoName.isEmpty()) {
                Toast.makeText(this, "El nombre del aposento no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar si el aposento ya existe en la lista
                if (aposentosList.contains(aposentoName)) {
                    Toast.makeText(this, "El aposento ya existe para este usuario", Toast.LENGTH_SHORT).show()
                } else {
                    // Agregar el aposento a la lista si no existe
                    dbManager.addAposento(aposentoName, currentUser)
                    aposentosList.add(aposentoName)

                    // Actualizar el TextView que muestra la lista de aposentos
                    binding.aposentosTextView.text = aposentosList.joinToString("\n")

                    // Limpiar el campo de texto
                    binding.aposentoEditText.text.clear()

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Aposento agregado exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.homebutton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", currentUser)
            startActivity(intent)
        }
    }

    private fun loadAposentos() {
        aposentosList.clear()
        aposentosList.addAll(dbManager.getAposentosByUser(currentUser))
        binding.aposentosTextView.text = aposentosList.joinToString("\n")
    }
}
