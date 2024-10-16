package com.example.samrthometec
/*
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabaseLockedException
import java.io.File
import android.util.Log
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseManager(private val context: Context) {

    // Nombre y ruta de la base de datos
    private val DATABASE_NAME = "AppMovil.db"

    @SuppressLint("SdCardPath")
    private val DATABASE_PATH = "/data/data/${context.packageName}/databases/"
    private val DATABASE_VERSION = 1

    init {
        copyDatabaseIfNeeded()
        //updateDatabase()
    }

    // Copia la base de datos desde assets si no existe
    private fun copyDatabaseIfNeeded() {
        val dbFile = File(DATABASE_PATH + DATABASE_NAME)
        if (!dbFile.exists()) {
            Log.d("DatabaseManager", "Database not found, copying from assets...")
            // Crear la carpeta "databases" si no existe
            File(DATABASE_PATH).mkdirs()

            try {
                // Copiar la base de datos desde assets
                val inputStream: InputStream = context.assets.open(DATABASE_NAME)
                val outputStream: OutputStream = FileOutputStream(dbFile)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
                Log.d("DatabaseManager", "Database copy successful.")
            } catch (e: Exception) {
                Log.e("DatabaseManager", "Error copying database: ${e.message}")
            }
        } else {
            Log.d("DatabaseManager", "Database already exists, no need to copy.")
        }
    }

    // Abre la base de datos existente
    fun openDatabase(): SQLiteDatabase {
        var db: SQLiteDatabase? = null
        var attempts = 0
        while (db == null && attempts < 5) {
            try {
                db = SQLiteDatabase.openDatabase(
                    DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
                )
            } catch (e: SQLiteDatabaseLockedException) {
                Log.e("DatabaseManager", "Database locked, retrying... (attempt ${attempts + 1})")
                Thread.sleep(50) // espera antes de reintentar
                attempts++
            }
        }
        if (db == null) {
            throw SQLiteDatabaseLockedException("Unable to open database after multiple attempts.")
        }
        Log.d("DatabaseManager", "Opening database at: $DATABASE_PATH")
        createTables(db) // Asegura que las tablas sean creadas si no existen
        return db
    }


    // Método para crear la tabla si no existe
    private fun createTables(db: SQLiteDatabase) {
        try {
            Log.d("DatabaseManager", "Creating tables if not exist.")
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS Aposentos (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Nombre TEXT NOT NULL UNIQUE,
                    UsuarioAso TEXT,
                    FOREIGN KEY(UsuarioAso) REFERENCES Cliente(username)
                );
            """.trimIndent()
            )
            Log.d("DatabaseManager", "Tables created or already exist.")
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error creating tables: ${e.message}")
        }
    }

    fun getAposentosByUser(username: String): List<String> {
        val aposentosList = mutableListOf<String>()
        val db = openDatabase()
        try {
            Log.d("DatabaseManager", "Fetching Aposentos for user: $username")
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


    fun addAposento(nombre: String, usuarioAso: String) {
        val db = openDatabase()
        try {
            db.beginTransaction() // Inicia la transacción
            Log.d("DatabaseManager", "Transaction started.")
            val values = ContentValues().apply {
                put("Nombre", nombre)
                put("UsuarioAso", usuarioAso)
            }
            val result = db.insert("Aposentos", null, values)
            if (result == -1L) {
                Log.e("DatabaseManager", "Error inserting Aposento: $nombre")
            } else {
                Log.d("DatabaseManager", "Aposento added: $nombre for user $usuarioAso")
                db.setTransactionSuccessful() // Marca la transacción como exitosa
                Log.d("DatabaseManager", "Transaction marked as successful.")

                // Llamar a checkAposentoExists después de la inserción
                val exists = checkAposentoExists(nombre)
                if (exists) {
                    Log.d("DatabaseManager", "Aposento $nombre exists in the database.")
                } else {
                    Log.e("DatabaseManager", "Aposento $nombre does not exist in the database.")
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error adding Aposento: ${e.message}")
        } finally {
            db.endTransaction() // Termina la transacción
            Log.d("DatabaseManager", "Transaction ended.")
            db.close()
        }
    }

    fun checkAposentoExists(nombre: String): Boolean {
        val db = openDatabase()
        var exists = false
        try {
            val cursor = db.rawQuery("SELECT 1 FROM Aposentos WHERE Nombre = ?", arrayOf(nombre))
            exists = cursor.moveToFirst()
            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error checking Aposento: ${e.message}")
        } finally {
            db.close()
        }
        return exists
    }

}

*/

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
                put("Numero Serie", serialNumber)
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
                "SELECT Descripcion, Tipo, Marca, `Numero Serie`, Consumo, IdAposento FROM Dispositivos WHERE UsuarioAso = ?",
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


}