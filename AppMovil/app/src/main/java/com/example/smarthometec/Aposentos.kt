package com.example.samrthometec

import android.R
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityAposentosBinding
import com.example.samrthometec.DatabaseManager

class Aposentos : AppCompatActivity() {

    private val aposentosList = mutableListOf<String>()
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private lateinit var binding: ActivityAposentosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAposentosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: ""

        loadAposentos()
        loadAposentosNombres()

        // Configuración del botón para agregar aposentos
        binding.addAposentoButton.setOnClickListener {
            val aposentoName = binding.aposentoEditText.text.toString().trim()

            if (aposentoName.isEmpty()) {
                Toast.makeText(this, "El nombre del aposento no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else if (aposentosList.contains(aposentoName)) {
                Toast.makeText(this, "El aposento ya existe para este usuario", Toast.LENGTH_SHORT).show()
            } else {
                dbManager.addAposento(aposentoName, currentUser)
                aposentosList.add(aposentoName)
                loadAposentos() // Recargar aposentos en el spinner
                binding.aposentosTextView.text = aposentosList.joinToString("\n")

                Toast.makeText(this, "Aposento agregado exitosamente", Toast.LENGTH_SHORT).show()
                binding.aposentoEditText.text.clear()
            }
        }

        // Listener del Spinner para cargar dispositivos asociados al aposento seleccionado
        binding.aposentoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedAposento = aposentosList[position]
                loadDevicesForAposento(selectedAposento)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no se selecciona ningún aposento
            }
        }

        // Configuración del botón "Inicio"
        binding.homebutton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", currentUser)
            startActivity(intent)
        }
    }

    // Cargar aposentos del usuario actual en el spinner
    private fun loadAposentos() {
        aposentosList.clear()
        aposentosList.add("Seleccione un aposento") // Agregar opción predeterminada
        aposentosList.addAll(dbManager.getAposentosByUser(currentUser)) // Obtener aposentos desde la base de datos
        updateAposentoSpinner()
    }

    // Actualizar el Spinner de aposentos
    private fun updateAposentoSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, aposentosList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.aposentoSpinner.adapter = adapter
    }


    // Cargar dispositivos asociados al aposento seleccionado
    private fun loadDevicesForAposento(aposento: String) {
        val devices = dbManager.getDevicesForAposento(currentUser, aposento)
        binding.devicesTextView.text = devices.joinToString("\n") { "${it.description} (Serial: ${it.numeroSerie}, Tipo: ${it.tipo})" }
    }
    private fun loadAposentosNombres() {
        aposentosList.clear()
        aposentosList.addAll(dbManager.getAposentosByUser(currentUser))
        binding.aposentosTextView.text = aposentosList.joinToString("\n")
    }
}
