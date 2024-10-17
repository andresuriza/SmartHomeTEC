package com.example.samrthometec


import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

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
    fun addDevice(description: String, type: String, brand: String, serialNumber: Int, consumption: Int, roomId: Int, username: String) {
        val db = dbHelper.writableDatabase
        try {
            val values = ContentValues().apply {
                put("Descripcion", description)
                put("Tipo", type)
                put("Marca", brand)
                put("NumeroSerie", serialNumber)
                put("Consumo", consumption)
                put("IdAposento", roomId)
                put("UsuarioAso", username)
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
                "SELECT Descripcion, Tipo, Marca, NumeroSerie, Consumo, IdAposento FROM Dispositivos WHERE UsuarioAso = ?",
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
    fun getRoomIdByName(roomName: String): Int {
        val db = dbHelper.readableDatabase
        var roomId = -1
        try {
            val cursor = db.rawQuery("SELECT ID FROM Aposentos WHERE Nombre = ?", arrayOf(roomName))
            if (cursor.moveToFirst()) {
                roomId = cursor.getInt(0)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error fetching room ID: ${e.message}")
        } finally {
            db.close()
        }
        return roomId
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


}