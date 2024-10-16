
package com.example.samrthometec
/*

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityAssociateDeviceBinding
import java.text.SimpleDateFormat
import java.util.*

class AssociateDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssociateDeviceBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private val associatedDevicesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssociateDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: ""

        loadAssociatedDevices()

        binding.associateDeviceButton.setOnClickListener {
            val description = binding.deviceDescriptionEditText.text.toString().trim()
            val type = binding.deviceTypeEditText.text.toString().trim()
            val brand = binding.deviceBrandEditText.text.toString().trim()
            val serialNumber = binding.deviceSerialEditText.text.toString().trim()
            val consumption = binding.deviceConsumptionEditText.text.toString().trim()
            val room = binding.deviceRoomEditText.text.toString().trim()

            if (description.isEmpty() || type.isEmpty() || brand.isEmpty() || serialNumber.isEmpty() || consumption.isEmpty() || room.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Calcular la fecha de garantía
                val currentDate = Date()
                val guaranteePeriod = getGuaranteePeriodForType(type)
                val guaranteeEndDate = Calendar.getInstance().apply {
                    time = currentDate
                    add(Calendar.MONTH, guaranteePeriod)
                }.time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedEndDate = dateFormat.format(guaranteeEndDate)

                // Agregar el dispositivo a la base de datos
                dbManager.addDevice(description, type, brand, serialNumber, consumption, room, formattedEndDate, currentUser)
                associatedDevicesList.add("$description ($type, $brand, $serialNumber, Consumo: $consumption, Aposento: $room, Garantía: $formattedEndDate)")
                binding.associatedDevicesTextView.text = associatedDevicesList.joinToString("\n")

                // Limpiar los campos de texto
                binding.deviceDescriptionEditText.text.clear()
                binding.deviceTypeEditText.text.clear()
                binding.deviceBrandEditText.text.clear()
                binding.deviceSerialEditText.text.clear()
                binding.deviceConsumptionEditText.text.clear()
                binding.deviceRoomEditText.text.clear()

                // Mostrar mensaje de éxito
                Toast.makeText(this, "Dispositivo asociado exitosamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAssociatedDevices() {
        // Cargar dispositivos asociados del usuario actual desde la base de datos
        associatedDevicesList.clear()
        associatedDevicesList.addAll(dbManager.getDevicesByUser(currentUser))
        binding.associatedDevicesTextView.text = associatedDevicesList.joinToString("\n")
    }

    private fun getGuaranteePeriodForType(type: String): Int {
        // Asigna los períodos de garantía según el tipo de dispositivo
        return when (type.toLowerCase(Locale.ROOT)) {
            "electrodoméstico" -> 24
            "electrónica" -> 12
            else -> 6
        }
    }
}
*/
