package com.example.samrthometec

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION,) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear las tablas
        db.execSQL("""CREATE TABLE IF NOT EXISTS Aposentos (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Nombre TEXT NOT NULL ,
            UsuarioAso TEXT,
            FOREIGN KEY(UsuarioAso) REFERENCES Cliente(username)
        )""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Manejar la actualización de la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS Aposentos")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1

        private const val DATABASE_NAME = "AppMovil.db"
    }
}
