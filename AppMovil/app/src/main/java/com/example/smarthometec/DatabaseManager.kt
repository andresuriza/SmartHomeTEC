package com.example.samrthometec


import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smarthometec.RetrofitInstance
import com.example.smarthometec.Sync
import com.example.smarthometec.Users
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val TAG = "DatabaseManager"

// Maneja las funciones de bases de datos
class DatabaseManager(private val context: Context) : AppCompatActivity() {
    private val dbHelper = MyDatabaseHelper(context)

    init {
        copyDatabaseIfNeeded()
    }

    private fun copyDatabaseIfNeeded() {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        if (!dbPath.exists()) {
            Log.d("DatabaseManager", "Database not found, copying from assets...")
            context.assets.open(DATABASE_NAME).use { inputStream ->
                FileOutputStream(dbPath).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    outputStream.flush()
                    Log.d("DatabaseManager", "Database copy successful.")
                }
            }
        } else {
            Log.d("DatabaseManager", "Database already exists, no need to copy.")
        }
    }
    fun checkDatabase(): Boolean {
        val dbFile = File(DATABASE_PATH + DATABASE_NAME)
        return dbFile.exists()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AppMovil.db"
        @SuppressLint("SdCardPath")
        private const val DATABASE_PATH = "/data/data/com.example.samrthometec/databases/"
    }



    fun copyDatabaseToLocalFolder() {
        val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
        val localPath = "C:/Users/gabri/Downloads/BaseNueva/"
        val destinationFilePath = localPath + DATABASE_NAME

        val source = File(dbPath)
        val destination = File(destinationFilePath)

        // Crea la carpeta si no existe
        val destinationDirectory = File(localPath)
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs()
        }

        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output)
            }
        }
        Log.d("DatabaseManager", "Database copied to: $destinationFilePath")
    }

 fun getAposentosByUser(username: String): List<String> {
     val aposentosList = mutableListOf<String>()
     val db = dbHelper.readableDatabase
     try {
         val cursor = db.rawQuery("SELECT Nombre FROM Aposentos WHERE UsuarioAso = ?", arrayOf(username))
         if (cursor.moveToFirst()) {
             do {
                 aposentosList.add(cursor.getString(0))
             } while (cursor.moveToNext())
         }
         cursor.close()

         // Si no hay aposentos, agregar predeterminados
         if (aposentosList.isEmpty()) {
             // Aposentos predeterminados
             val defaultAposentos = listOf("Dormitorio", "Cocina", "Sala", "Comedor")
             for (aposento in defaultAposentos) {
                 addAposento(aposento, username) // Agregar a la base de datos
                 aposentosList.add(aposento) // Agregar a la lista
             }
             Toast.makeText(context, "Se han agregado aposentos predeterminados", Toast.LENGTH_SHORT).show()
         }
     } catch (e: Exception) {
         Log.e("DatabaseManager", "Error fetching Aposentos: ${e.message}")
     } finally {
         db.close()
     }
     return aposentosList
 }


// Agrega aposento
    fun addAposento(nombre: String, usuarioAso: String) {
        val db = dbHelper.writableDatabase
        try {
            Log.d("DatabaseManager", "Attempting to add aposento: $nombre for user $usuarioAso")
            Log.d("DatabaseManager", "Database path: ${db.path}") // Verificar la ruta de la base de datos

            val values = ContentValues().apply {
                put("Nombre", nombre)
                put("UsuarioAso", usuarioAso)
            }

            val result = db.insert("Aposentos", null, values)
            if (result == -1L) {
                Log.e("DatabaseManager", "Error inserting Aposento: $nombre")
            } else {
                Log.d("DatabaseManager", "Aposento added: $nombre for user $usuarioAso")
            }

            // Verificar si los datos se guardaron correctamente
            val cursor = db.rawQuery("SELECT * FROM Aposentos WHERE Nombre = ?", arrayOf(nombre))
            if (cursor.moveToFirst()) {
                Log.d("DatabaseManager", "Inserted aposento found in database: ${cursor.getString(0)}")
                copyDatabaseToLocalFolder()
            } else {
                Log.e("DatabaseManager", "Inserted aposento not found in database")
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error adding Aposento: ${e.message}")
        } finally {
            db.close()
        }
    }

    // Agrega usuarios
    fun addUser() {
        var usuario: List<Users>

        if (Sync.isOnline) {
            val db = dbHelper.writableDatabase

            lifecycleScope.launch {
                val response = try {
                    RetrofitInstance.api.getUsers()
                } catch (e: IOException) {
                    Log.e(TAG, "IOException, you might not have internet")
                    return@launch
                } catch (e: HttpException) {
                    Log.e(TAG, "HTTP Exception, error in api")
                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    usuario = response.body()!!

                    for (u in usuario) {
                        val values = ContentValues().apply {
                            put("correo", u.correoElectronico)
                            put("password", u.contrasena)
                            put("region", u.region)
                            put("Nombre", u.nombre)
                            put("Apellido1", u.apellidos)
                            put("Apellido2", "") // CAMBIAR EN POSTGRES
                        }

                        db.insert("Cliente", null, values)
                    }
                } else {
                    Log.e(TAG, "Response not successful")
                }
            }
        }
    }

    // Método para verificar si un usuario existe en la base de datos
    fun checkUser(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        var exists = false
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM Cliente WHERE correo = ? AND password = ?",
                arrayOf(username, password)
            )
            exists = cursor.moveToFirst()
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error checking user: ${e.message}")
        } finally {
            db.close()
        }
        return exists

    }

    // Agrega dispositivos
    fun addDevice(description: String, typeId: Int, brand: Int, serialNumber: Int, consumption: Int, roomId: Int, username: String, Fecha: String, FechaGarantia: String) {
        val db = dbHelper.writableDatabase
        try {
            val values = ContentValues().apply {
                put("Descripcion", description)
                put("IDTipo", typeId)
                put("Marca", brand)
                put("NumeroSerie", serialNumber)
                put("Consumo", consumption)
                put("IdAposento", roomId)
                put("UsuarioAso", username)
                put("FechaAsocie", Fecha)
                put("FechaGarantiaFin", FechaGarantia)
            }
            val result = db.insert("Dispositivos", null, values)
            if (result == -1L) {
                Log.e("DatabaseManager", "Error inserting Device: $description")
            } else {
                Log.d("DatabaseManager", "Device added: $description for user $username")
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error adding Device: ${e.message}")
        } finally {
            db.close()
        }

    }

    // Obtiene los dispositivos por usuario
    fun getDevicesByUser(username: String): List<String> {
        val devicesList = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery(
                "SELECT Descripcion, IDTipo, Marca, NumeroSerie, Consumo, IdAposento FROM Dispositivos WHERE UsuarioAso = ?",
                arrayOf(username)
            )
            if (cursor.moveToFirst()) {
                do {
                    val device = "${cursor.getString(0)} (${cursor.getString(1)}, ${cursor.getString(2)}, ${cursor.getInt(3)}, Consumo: ${cursor.getInt(4)}, Aposento ID: ${cursor.getInt(5)})"
                    devicesList.add(device)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching Devices: ${e.message}")
        } finally {
            db.close()
        }
        return devicesList
    }

    // Obtiene marcas de dispositivos
    fun getAllBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Marca", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID")) // Usa getColumnIndexOrThrow
                val name = cursor.getString(cursor.getColumnIndexOrThrow("Nombre")) // Usa getColumnIndexOrThrow
                brands.add(Brand(id, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return brands
    }

    data class Brand(val id: Int, val name: String)
    data class Aposento(val id: Int, val name: String)
    data class User(val username: String)
    data class DeviceType(val id: Int, val name: String)


    fun getAposentosSpin (username: String): List<Aposento> {
        val aposentosList = mutableListOf<Aposento>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT ID, Nombre FROM Aposentos WHERE UsuarioAso = ?", arrayOf(username))
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                    aposentosList.add(Aposento(id, name))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching Aposentos: ${e.message}")
        } finally {
            db.close()
        }
        return aposentosList
    }

    fun getAllUsersExcept(currentUser: String): List<User> {
        val users = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery(
                "SELECT correo FROM Cliente WHERE correo != ?",
                arrayOf(currentUser)
            )
            if (cursor.moveToFirst()) {
                do {
                    val username = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                    users.add(User(username))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching users: ${e.message}")
        } finally {
            db.close()
        }
        return users
    }
    fun getAllDeviceTypes(): List<DeviceType> {
        val deviceTypes = mutableListOf<DeviceType>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT ID, Nombre FROM Tipo", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                deviceTypes.add(DeviceType(id, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return deviceTypes
    }
    // En DatabaseManager
    fun getGuaranteePeriodByTypeId(typeId: Int): Int {
        var guaranteePeriod = 6 // Valor predeterminado en caso de no encontrar el tipo
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT TiempoGarantia FROM Tipo WHERE ID = ?", arrayOf(typeId.toString()))

        if (cursor.moveToFirst()) {
            guaranteePeriod = cursor.getInt(cursor.getColumnIndexOrThrow("TiempoGarantia"))
        }
        cursor.close()
        return guaranteePeriod
    }

    // Funcion que transfiere dispositivo
    fun transferDevice(deviceString: String, currentUser: String, newOwner: String) {
        val db = dbHelper.writableDatabase
        try {
            // Obtener el NumeroSerie del dispositivo a partir de la selección
            val numeroSerie = deviceString.split("Serial: ")[1].toInt()  // Extraer el NumeroSerie del formato

            Log.d("TransferDevice", "Iniciando transferencia del dispositivo: $numeroSerie del usuario $currentUser al nuevo dueño $newOwner")

            // Obtener la fecha de garantía y la fecha de asociación desde la tabla Dispositivos
            val cursor = db.rawQuery("SELECT FechaAsocie, FechaGarantiaFin FROM Dispositivos WHERE NumeroSerie = ? AND UsuarioAso = ?", arrayOf(numeroSerie.toString(), currentUser))

            if (cursor.moveToFirst()) {
                val fechaAsocie = cursor.getString(cursor.getColumnIndexOrThrow("FechaAsocie"))
                val fechaGarantiaFin = cursor.getString(cursor.getColumnIndexOrThrow("FechaGarantiaFin"))

                Log.d("TransferDevice", "Dispositivo encontrado: $numeroSerie, Fecha Asocie: $fechaAsocie, Fecha Garantía Fin: $fechaGarantiaFin")

                // Registrar la transferencia en la tabla UsuariosHistoricos
                val currentDate = System.currentTimeMillis() // Obtener la fecha actual para la transferencia
                val transferDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentDate)

                val contentValues = ContentValues().apply {
                    put("NSerie", numeroSerie)
                    put("UsuarioActual", newOwner)
                    put("UsuarioAnterior", currentUser)
                    put("FechaAsocie", fechaAsocie)
                    put("FechaIntercambio", transferDate)
                    put("FechaGarantiaFin", fechaGarantiaFin)
                }

                val insertResult = db.insert("UsuariosHistoricos", null, contentValues)
                if (insertResult == -1L) {
                    Log.e("TransferDevice", "Error insertando en UsuariosHistoricos")
                } else {
                    Log.d("TransferDevice", "Registro insertado en UsuariosHistoricos correctamente")
                }

                // Actualizar el usuario actual en la tabla Dispositivos
                val updateValues = ContentValues().apply {
                    put("UsuarioAso", newOwner)
                }

                val updateResult = db.update("Dispositivos", updateValues, "NumeroSerie = ?", arrayOf(numeroSerie.toString()))
                if (updateResult > 0) {
                    Log.d("TransferDevice", "Usuario actualizado correctamente en Dispositivos")
                } else {
                    Log.e("TransferDevice", "Error actualizando UsuarioAso en Dispositivos")
                }

                cursor.close()
            } else {
                Log.e("TransferDevice", "Dispositivo no encontrado en la base de datos")
                Toast.makeText(context, "Dispositivo no encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("TransferDevice", "Error transfiriendo dispositivo: ${e.message}")
        } finally {
            db.close()
        }
    }


    fun getDevicesWithTypeByUser(username: String): List<Device> {
        val devicesList = mutableListOf<Device>()
        val db = dbHelper.readableDatabase
        try {
            // Consulta para obtener dispositivos del usuario, y el nombre del tipo desde la tabla Tipo
            val cursor = db.rawQuery(
                """
            SELECT d.Descripcion, d.NumeroSerie, t.Nombre 
            FROM Dispositivos d 
            INNER JOIN Tipo t ON d.IDTipo = t.ID
            WHERE d.UsuarioAso = ?
            """, arrayOf(username)
            )

            if (cursor.moveToFirst()) {
                do {
                    val description = cursor.getString(cursor.getColumnIndexOrThrow("Descripcion"))
                    val serialNumber = cursor.getInt(cursor.getColumnIndexOrThrow("NumeroSerie"))
                    val tipo = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))

                    devicesList.add(Device(description, tipo, serialNumber))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching Devices with Type: ${e.message}")
        } finally {
            db.close()
        }
        return devicesList
    }


    fun recordStartTime(numeroSerie: Int, startTime: Long) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("NSerie", numeroSerie)
            put("FechaDeEncendido", SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(startTime)))
            putNull("TiempoEncendido")
        }
        db.insert("TiempoEncendido", null, contentValues)
        db.close()
    }

    // Obtener el tiempo de encendido (Fecha y hora de encendido)
    fun getStartTime(numeroSerie: Int): Long {
        val db = dbHelper.readableDatabase
        var startTime: Long = 0
        val cursor = db.rawQuery("SELECT FechaDeEncendido FROM TiempoEncendido WHERE NSerie = ? AND TiempoEncendido IS NULL", arrayOf(numeroSerie.toString()))
        if (cursor.moveToFirst()) {
            val dateString = cursor.getString(cursor.getColumnIndexOrThrow("FechaDeEncendido"))
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            startTime = dateFormat.parse(dateString).time
        }
        cursor.close()
        db.close()
        return startTime
    }

    // Registrar el tiempo total encendido cuando se apaga el dispositivo
    fun recordDeviceUsage(numeroSerie: Int, elapsedTime: Int) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("TiempoEncendido", elapsedTime) // Guardar el tiempo total que estuvo encendido
        }
        db.update("TiempoEncendido", contentValues, "NSerie = ? AND TiempoEncendido IS NULL", arrayOf(numeroSerie.toString()))
        db.close()
    }

    // Verificar si el dispositivo está encendido
    fun isDeviceOn(numeroSerie: Int): Boolean {
        val db = dbHelper.readableDatabase
        var isOn = false
        val cursor = db.rawQuery("SELECT FechaDeEncendido FROM TiempoEncendido WHERE NSerie = ? AND TiempoEncendido IS NULL", arrayOf(numeroSerie.toString()))
        if (cursor.moveToFirst()) {
            isOn = true // Si hay una fecha de encendido y no hay tiempo registrado, está encendido
        }
        cursor.close()
        db.close()
        return isOn
    }
    //Obtener Dispositivos asociados a un aposento
    fun getDevicesForAposento(username: String, aposento: String): List<Device> {
        val devicesList = mutableListOf<Device>()
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery(
                """
            SELECT d.Descripcion, d.NumeroSerie, t.Nombre 
            FROM Dispositivos d 
            INNER JOIN Tipo t ON d.IDTipo = t.ID
            WHERE d.UsuarioAso = ? AND d.IdAposento = (SELECT ID FROM Aposentos WHERE Nombre = ?)
            """, arrayOf(username, aposento)//Se hace un JOIN para obtener el nombre del tipo de dispositivo, y se filtra por el aposento
            )

            if (cursor.moveToFirst()) {
                do {
                    val description = cursor.getString(cursor.getColumnIndexOrThrow("Descripcion"))
                    val serialNumber = cursor.getInt(cursor.getColumnIndexOrThrow("NumeroSerie"))
                    val tipo = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))

                    devicesList.add(Device(description, tipo, serialNumber))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching Devices for Aposento: ${e.message}")
        } finally {
            db.close()
        }
        return devicesList
    }
    fun deleteDevice(numeroSerie: Int) {
        val db = dbHelper.writableDatabase
        try {
            db.delete("Dispositivos", "NumeroSerie = ?", arrayOf(numeroSerie.toString()))
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error deleting device: ${e.message}")
        } finally {
            db.close()
        }
    }
    fun getUserInfo(correo: String): Usuario {
        val db = dbHelper.readableDatabase
        var usuario: Usuario? = null

        try {
            val cursor = db.rawQuery("SELECT * FROM Cliente WHERE correo = ?", arrayOf(correo))
            if (cursor.moveToFirst()) {
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val region = cursor.getString(cursor.getColumnIndexOrThrow("region"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"))
                val apellido1 = cursor.getString(cursor.getColumnIndexOrThrow("Apellido1"))
                val apellido2 = cursor.getString(cursor.getColumnIndexOrThrow("Apellido2"))

                usuario = Usuario(correo, password, region, nombre, apellido1, apellido2)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching user info: ${e.message}")
        } finally {
            db.close()
        }

        return usuario ?: throw Exception("Usuario no encontrado")
    }
    data class Usuario(
        val correo: String,
        val password: String,
        val region: String,
        val nombre: String,
        val apellido1: String,
        val apellido2: String
    )

    // Clase de dispositivo que incluye la descripción, tipo y número de serie
    data class Device(val description: String, val tipo: String, val numeroSerie: Int)
}













