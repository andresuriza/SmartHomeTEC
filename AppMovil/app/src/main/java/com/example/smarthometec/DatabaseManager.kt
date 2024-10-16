package com.example.smarthometec

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
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
        val db = SQLiteDatabase.openDatabase(
            DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
        )
        Log.d("DatabaseManager", "Opening database at: $DATABASE_PATH")
        createTables(db) // Asegura que las tablas sean creadas si no existen
        return db
    }

    // Método para crear la tabla si no existe
    private fun createTables(db: SQLiteDatabase) {
        try {
            Log.d("DatabaseManager", "Creating tables if not exist.")
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS Aposentos (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Nombre TEXT NOT NULL UNIQUE,
                    UsuarioAso TEXT,
                    FOREIGN KEY(UsuarioAso) REFERENCES Cliente(username)
                );
            """.trimIndent())
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
            val cursor = db.rawQuery("SELECT Nombre FROM Aposentos WHERE UsuarioAso = ?", arrayOf(username))

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

    // Método para agregar un nuevo aposento
/*    fun addAposento(nombre: String, usuarioAso: String) {
        val db = openDatabase()
        try {
            val values = ContentValues().apply {
                put("Nombre", nombre)
                put("UsuarioAso", usuarioAso)
            }
            db.insert("Aposentos", null, values)
            Log.d("DatabaseManager", "Aposento added: $nombre for user $usuarioAso")
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error adding Aposento: ${e.message}")
        } finally {
            db.close()
        }
    }*/
    fun addAposento(nombre: String, usuarioAso: String) {
        val db = openDatabase()
        try {
            val values = ContentValues().apply {
                put("Nombre", nombre)
                put("UsuarioAso", usuarioAso)
            }

            // Realiza la inserción y guarda el resultado
            val result = db.insert("Aposentos", null, values)

            // Verifica si la inserción fue exitosa
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
    }


    fun updateDatabase() {
        val db = openDatabase()
        try {
            Log.d("DatabaseManager", "Updating database: Dropping and recreating tables.")
            db.execSQL("DROP TABLE IF EXISTS Aposentos")
            //createTables(db)
        } catch (e: Exception) {
            Log.e("DatabaseManager", "Error updating database: ${e.message}")
        } finally {
            db.close()
        }
    }
}



/*
package com.example.smarthometec

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.io.File
import android.util.Log
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseManager(private val context: Context) {

    // Nombre y ruta de la base de datos
    private val DATABASE_NAME = "AppMovil.db"
    private val DATABASE_PATH = "/data/data/${context.packageName}/databases/"
    private val DATABASE_VERSION = 1

    init {
        copyDatabaseIfNeeded()
    }

    // Copia la base de datos desde assets si no existe
    private fun copyDatabaseIfNeeded() {
        val dbFile = File(DATABASE_PATH + DATABASE_NAME)
        if (!dbFile.exists()) {
            Log.d("DatabaseManager", "Database not found, copying from assets...")
            // Crear la carpeta "databases" si no existe
            File(DATABASE_PATH).mkdirs()

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
        }
    }

    // Abre la base de datos existente
    fun openDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(
            DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE

        )
        Log.d("DatabaseManager", "Opening database at: $DATABASE_PATH")
    }
    private fun createTables(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Aposentos (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Nombre TEXT NOT NULL UNIQUE,
                UsuarioAso TEXT,
                FOREIGN KEY(UsuarioAso) REFERENCES Cliente(username)
            );
        """.trimIndent())
    }

    fun getAposentosByUser(username: String): List<String> {
        val aposentosList = mutableListOf<String>()
        val db = openDatabase()
        val cursor = db.rawQuery("SELECT Nombre FROM Aposentos WHERE UsuarioAso = ?", arrayOf(username))

        if (cursor.moveToFirst()) {
            do {
                aposentosList.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return aposentosList
    }

    // Método para agregar un nuevo aposento
    fun addAposento(nombre: String, usuarioAso: String) {
        val db = openDatabase()
        val values = ContentValues().apply {
            put("Nombre", nombre)
            put("UsuarioAso", usuarioAso)
        }
        db.insert("Aposentos", null, values)
        db.close()
    }
    fun updateDatabase() {
        val db = openDatabase()
        // Aquí puedes manejar cambios entre versiones, si fuera necesario
        db.execSQL("DROP TABLE IF EXISTS Aposentos")
        createTables(db)
        db.close()
    }

}
*/
