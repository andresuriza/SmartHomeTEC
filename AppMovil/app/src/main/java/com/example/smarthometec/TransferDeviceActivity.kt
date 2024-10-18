package com.example.samrthometec

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityTransferDeviceBinding

class TransferDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferDeviceBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private val deviceMap = mutableListOf<String>() // Lista para guardar los dispositivos (sin ID)
    private val newOwnerIdMap = mutableMapOf<String, String>() // Mapa para los dueños nuevos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: ""

        // Mostrar el dueño actual
        binding.currentOwnerTextView.text = currentUser

        loadDevices()  // Cargar los dispositivos asociados al usuario actual
        loadNewOwners() // Cargar lista de nuevos dueños

        // Configurar el botón de transferencia
        binding.transferButton.setOnClickListener {
            val selectedDevice = binding.deviceSpinner.selectedItem.toString()
            val selectedNewOwner = binding.newOwnerSpinner.selectedItem.toString()

            if (selectedDevice.isEmpty() || selectedNewOwner.isEmpty()) {
                Toast.makeText(this, "Debes seleccionar un dispositivo y un nuevo dueño", Toast.LENGTH_SHORT).show()
            } else {
                // Realizar la transferencia en la base de datos (sin ID, pero con el nombre del dispositivo)
                dbManager.transferDevice(selectedDevice, currentUser, selectedNewOwner)
                Toast.makeText(this, "Dispositivo transferido exitosamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Cargar dispositivos del dueño actual (currentUser)
    private fun loadDevices() {
        val deviceList = dbManager.getDevicesByUser(currentUser) // Obtener dispositivos del usuario actual

        deviceMap.clear()
        deviceMap.addAll(deviceList)  // No mapeamos por ID, solo usamos los nombres ya formateados

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceMap)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.deviceSpinner.adapter = adapter
    }

    // Cargar nuevos dueños posibles
    private fun loadNewOwners() {
        val ownerList = dbManager.getAllUsersExcept(currentUser) // Obtener usuarios, excepto el actual
        val ownerNames = mutableListOf<String>()

        for (owner in ownerList) {
            ownerNames.add(owner.username)
            newOwnerIdMap[owner.username] = owner.username  // Almacenar el nuevo dueño en el mapa
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ownerNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.newOwnerSpinner.adapter = adapter
    }
}

