package com.example.samrthometec


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityAssociateDeviceBinding
import java.text.SimpleDateFormat
import java.util.*

class AssociateDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssociateDeviceBinding
    private lateinit var dbManager: DatabaseManager
    private lateinit var currentUser: String
    private val associatedDevicesList = mutableListOf<String>()
    private val brandIdMap = mutableMapOf<String, Int>() // Mapa para almacenar marca y su ID
    private val roomIdMap = mutableMapOf<String, Int>() // Mapa para almacenar aposento y su ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssociateDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DatabaseManager(this)
        currentUser = intent.getStringExtra("username") ?: ""

        loadBrands()
        loadAssociatedDevices()
        loadRooms()

        binding.associateDeviceButton.setOnClickListener {
            val description = binding.deviceDescriptionEditText.text.toString().trim()
            val type = binding.deviceTypeEditText.text.toString().trim()
            //val brand = binding.deviceBrandEditText.text.toString().trim()
            val selectedBrand = binding.brandSpinner.selectedItem.toString() // Marca seleccionada
            val brandId = brandIdMap[selectedBrand] // Obtener el ID de la marca
            val serialNumberStr = binding.deviceSerialEditText.text.toString().trim()
            val consumptionStr = binding.deviceConsumptionEditText.text.toString().trim()
            //val room = binding.deviceRoomEditText.text.toString().trim()
            val selectedRoom = binding.roomSpinner.selectedItem.toString() // Aposento seleccionado
            val roomId = roomIdMap[selectedRoom] // Obtener el ID del aposento


            if (description.isEmpty() || type.isEmpty() || serialNumberStr.isEmpty() || consumptionStr.isEmpty() || roomId == null) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val serialNumber = serialNumberStr.toInt()
                    val consumption = consumptionStr.toInt()
                    //val roomId = dbManager.getRoomIdByName(room)

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
                    dbManager.addDevice(description, type, selectedBrand, serialNumber, consumption, roomId, currentUser)
                    associatedDevicesList.add("$description ($type, $selectedBrand, $serialNumber, Consumo: $consumption, Aposento: $selectedRoom, Garantía: $formattedEndDate)")
                    binding.associatedDevicesTextView.text = associatedDevicesList.joinToString("\n")

                    // Limpiar los campos de texto
                    binding.deviceDescriptionEditText.text.clear()
                    binding.deviceTypeEditText.text.clear()
                    binding.deviceSerialEditText.text.clear()
                    binding.deviceConsumptionEditText.text.clear()
                    //binding.deviceRoomEditText.text.clear()

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Dispositivo asociado exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Número de serie y consumo deben ser números enteros", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun loadBrands() {
        val brandList = dbManager.getAllBrands() // Método que obtiene marcas de la base de datos
        val brandNames = mutableListOf<String>()

        // Agregar el hint como el primer elemento
        brandNames.add("Seleccione una marca") // Este será el hint

        // Llenar el mapa de marcas con su ID, empezando desde el segundo elemento
        for (brand in brandList) {
            brandNames.add(brand.name) // Solo el nombre
            brandIdMap[brand.name] = brand.id // Almacenar el ID de la marca con su nombre
        }

        // Crear el adaptador para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, brandNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.brandSpinner.adapter = adapter

        // Configurar el comportamiento cuando no se selecciona ninguna marca (hint seleccionado)
        binding.brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    // Hint seleccionado, no hacer nada o mostrar un mensaje
                } else {
                    // Marca seleccionada, puedes manejar la selección aquí
                    val selectedBrand = brandNames[position]
                    Toast.makeText(applicationContext, "Seleccionaste: $selectedBrand", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada, puedes manejarlo si es necesario
            }
        }
    }
    private fun loadRooms() {
        val roomList = dbManager.getAposentosSpin(currentUser) // Método que obtiene aposentos asociados al usuario
        val roomNames = mutableListOf<String>()

        // Agregar el hint como el primer elemento
        roomNames.add("Seleccione un aposento") // Este será el hint

        // Llenar el mapa de aposentos con su ID
        for (room in roomList) {
            roomNames.add(room.name) // Solo el nombre del aposento
            roomIdMap[room.name] = room.id // Almacenar el ID del aposento con su nombre
        }

        // Crear el adaptador para el Spinner de aposentos
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roomNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roomSpinner.adapter = adapter

        // Configurar el comportamiento cuando no se selecciona ningún aposento (hint seleccionado)
        binding.roomSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    // Hint seleccionado, no hacer nada
                } else {
                    // Aposento seleccionado, puedes manejar la selección aquí
                    val selectedRoom = roomNames[position]
                    Toast.makeText(applicationContext, "Seleccionaste: $selectedRoom", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada, puedes manejarlo si es necesario
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

