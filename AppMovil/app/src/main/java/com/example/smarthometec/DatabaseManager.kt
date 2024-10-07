package com.example.smarthometec

import android.content.Context
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
}
