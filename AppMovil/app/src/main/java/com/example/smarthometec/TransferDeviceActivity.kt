package com.example.samrthometec

import android.os.Bundle
import android.util.Log
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

            Log.d("TransferDevice", "Dispositivo seleccionado: $selectedDevice, Nuevo dueño seleccionado: $selectedNewOwner")

            if (selectedDevice == "Seleccione un dispositivo" || selectedNewOwner == "Seleccione un nuevo dueño") {
                Toast.makeText(this, "Debes seleccionar un dispositivo y un nuevo dueño", Toast.LENGTH_SHORT).show()
            } else {
                // Realizar la transferencia en la base de datos
                dbManager.transferDevice(selectedDevice, currentUser, selectedNewOwner)
                Toast.makeText(this, "Dispositivo transferido exitosamente", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun loadDevices() {
        val deviceList = dbManager.getDevicesWithTypeByUser(currentUser) // Usamos la nueva función para obtener dispositivos con tipo

        deviceMap.clear()
        deviceMap.add("Seleccione un dispositivo") // Añadir el hint al principio

        for (device in deviceList) {
            val deviceString = "${device.description}, ${device.tipo}, Serial: ${device.numeroSerie}"  // Mostrar Descripción, Tipo y NumeroSerie
            deviceMap.add(deviceString)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceMap)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.deviceSpinner.adapter = adapter
    }





    private fun loadNewOwners() {
        val ownerList = dbManager.getAllUsersExcept(currentUser) // Obtener usuarios, excepto el actual
        val ownerNames = mutableListOf<String>()

        ownerNames.add("Seleccione un nuevo dueño") // Añadir el hint al principio
        for (owner in ownerList) {
            ownerNames.add(owner.username)
            newOwnerIdMap[owner.username] = owner.username  // Almacenar el nuevo dueño en el mapa
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ownerNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.newOwnerSpinner.adapter = adapter
    }

}

