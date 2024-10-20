package com.example.samrthometec

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityDeviceControlBinding
import java.text.SimpleDateFormat
import java.util.*

class DeviceControlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceControlBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private val deviceMap = mutableListOf<DeviceStatus>()  // Lista para guardar dispositivos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: ""

        loadDevices()  // Cargar los dispositivos

        // Configurar el botón de encendido
        binding.turnOnButton.setOnClickListener {
            val selectedDevice = binding.deviceSpinner.selectedItem as DeviceStatus?
            selectedDevice?.let { device ->
                if (!dbManager.isDeviceOn(device.numeroSerie)) { // Comprobar si el dispositivo está apagado
                    turnOnDevice(device)
                } else {
                    Toast.makeText(this, "El dispositivo ya está encendido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Configurar el botón de apagado
        binding.turnOffButton.setOnClickListener {
            val selectedDevice = binding.deviceSpinner.selectedItem as DeviceStatus?
            selectedDevice?.let { device ->
                if (dbManager.isDeviceOn(device.numeroSerie)) { 
                    turnOffDevice(device)
                } else {
                    Toast.makeText(this, "El dispositivo ya está apagado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Cargar dispositivos del usuario actual
    private fun loadDevices() {
        val deviceList = dbManager.getDevicesWithTypeByUser(currentUser)

        deviceMap.clear()
        deviceMap.addAll(deviceList.map { DeviceStatus(it.numeroSerie, it.description) })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceMap)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.deviceSpinner.adapter = adapter
    }

    // Encender el dispositivo
    private fun turnOnDevice(device: DeviceStatus) {
        val currentTime = System.currentTimeMillis() // Tiempo actual como la fecha de encendido
        dbManager.recordStartTime(device.numeroSerie, currentTime) // Guardamos la fecha y hora de encendido
        Toast.makeText(this, "Dispositivo encendido", Toast.LENGTH_SHORT).show()
    }

    // Apagar el dispositivo y calcular el tiempo encendido
    private fun turnOffDevice(device: DeviceStatus) {
        val currentTime = System.currentTimeMillis()
        val startTime = dbManager.getStartTime(device.numeroSerie) // Obtener la fecha y hora en que se encendió el dispositivo
        val elapsedTime = currentTime - startTime // Calcular el tiempo encendido en milisegundos
        val elapsedTimeInSeconds = elapsedTime / 1000 // Convertir a segundos

        // Actualizar la tabla TiempoEncendido con el tiempo total encendido
        dbManager.recordDeviceUsage(device.numeroSerie, elapsedTimeInSeconds.toInt())

        Toast.makeText(this, "Dispositivo apagado, tiempo registrado", Toast.LENGTH_SHORT).show()
    }

    // Clase para representar el estado de un dispositivo
    data class DeviceStatus(val numeroSerie: Int, val description: String) {
        override fun toString(): String {
            return "$description, Serial: $numeroSerie"
        }
    }
}

