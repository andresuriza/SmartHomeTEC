package com.example.samrthometec

import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.samrthometec.databinding.ActivityDeviceControlBinding

class DeviceControlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceControlBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private val deviceMap = mutableListOf<DeviceStatus>()  // Lista para guardar dispositivos y su estado (on/off)

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
                if (!device.isOn) {
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
                if (device.isOn) {
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
        deviceMap.addAll(deviceList.map { DeviceStatus(it.numeroSerie, it.description, false) })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deviceMap)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.deviceSpinner.adapter = adapter
    }

    // Encender el dispositivo
    private fun turnOnDevice(device: DeviceStatus) {
        val currentTime = System.currentTimeMillis() // Tiempo actual como la fecha de encendido
        dbManager.updateDeviceStatus(device.numeroSerie, true, currentTime)
        device.isOn = true
        Toast.makeText(this, "Dispositivo encendido", Toast.LENGTH_SHORT).show()
    }

    // Apagar el dispositivo
    private fun turnOffDevice(device: DeviceStatus) {
        val currentTime = System.currentTimeMillis()
        val timeOn = dbManager.getTimeOn(device.numeroSerie) // Obtener tiempo que estuvo encendido
        dbManager.updateDeviceStatus(device.numeroSerie, false, currentTime)

        val elapsedTime = currentTime - timeOn // Calcular tiempo encendido en milisegundos
        val elapsedTimeInSeconds = elapsedTime / 1000 // Convertir a segundos

        dbManager.recordDeviceUsage(device.numeroSerie, timeOn, elapsedTimeInSeconds.toInt()) // Registrar en la tabla TiempoEncendido
        device.isOn = false
        Toast.makeText(this, "Dispositivo apagado, tiempo registrado", Toast.LENGTH_SHORT).show()
    }
}

data class DeviceStatus(val numeroSerie: Int, val description: String, var isOn: Boolean) {
    override fun toString(): String {
        return "$description, Serial: $numeroSerie"
    }
}
