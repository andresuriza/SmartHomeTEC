package com.example.samrthometec


import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseManager(private val context: Context) {
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
            val cursor =
                db.rawQuery("SELECT Nombre FROM Aposentos WHERE UsuarioAso = ?", arrayOf(username))
            if (cursor.moveToFirst()) {
                do {
                    aposentosList.add(cursor.getString(0))
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

/*    fun addAposento(nombre: String, usuarioAso: String) {
        val db = dbHelper.writableDatabase
        try {
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
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error adding Aposento: ${e.message}")
        } finally {
            db.close()
        }
    }*/
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

    // Método para verificar si un usuario existe en la base de datos


    fun checkUser(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        var exists = false
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM Cliente WHERE username = ? AND password = ?",
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
                "SELECT username FROM Cliente WHERE username != ?",
                arrayOf(currentUser)
            )
            if (cursor.moveToFirst()) {
                do {
                    val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
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
    // En DatabaseManager
    fun transferDevice(deviceName: String, currentUser: String, newOwner: String) {
        val db = dbHelper.writableDatabase
        try {
            // Obtener el número de serie, la fecha de garantía y la fecha de asociación desde la tabla Dispositivos
            val cursor = db.rawQuery("SELECT NumeroSerie, FechaAsocie, FechaGarantiaFin FROM Dispositivos WHERE Descripcion = ? AND UsuarioAso = ?", arrayOf(deviceName, currentUser))
            if (cursor.moveToFirst()) {
                val serialNumber = cursor.getInt(cursor.getColumnIndexOrThrow("NumeroSerie"))
                val fechaAsocie = cursor.getString(cursor.getColumnIndexOrThrow("FechaAsocie"))
                val fechaGarantiaFin = cursor.getString(cursor.getColumnIndexOrThrow("FechaGarantiaFin"))

                // Registrar la transferencia en la tabla UsuariosHistoricos
                val currentDate = System.currentTimeMillis() // Obtener la fecha actual para la transferencia
                val transferDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentDate)

                val contentValues = ContentValues().apply {
                    put("NSerie", serialNumber)
                    put("UsuarioActual", newOwner)
                    put("UsuarioAnterior", currentUser)
                    put("FechaAsocie", fechaAsocie)
                    put("FechaIntercambio", transferDate)
                    put("FechaGarantiaFin", fechaGarantiaFin)
                }
                db.insert("UsuariosHistoricos", null, contentValues)

                // Actualizar el usuario actual en la tabla Dispositivos
                val updateValues = ContentValues().apply {
                    put("UsuarioAso", newOwner)
                }
                db.update("Dispositivos", updateValues, "NumeroSerie = ?", arrayOf(serialNumber.toString()))

                cursor.close()
            } else {
                // Si no se encuentra el dispositivo, lanzar un mensaje de error
                Toast.makeText(context, "Dispositivo no encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error transferring device: ${e.message}")
        } finally {
            db.close()
        }
    }









}