package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthometec.Sync

// Clase que carga la primera pantalla a mostrar
class MainActivity : AppCompatActivity() {
    private lateinit var databaseManager: DatabaseManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referenciar los botones
        val loginButton: Button = findViewById(R.id.loginButton)
        val syncButton: ImageButton = findViewById(R.id.imageButton)

        databaseManager = DatabaseManager(this)
        if (!databaseManager.checkDatabase()) {
            Toast.makeText(this, "Database not found!", Toast.LENGTH_SHORT).show()
            return
        }

        // Configurar el botón para ir a la pantalla de Login
        loginButton.setOnClickListener {
            databaseManager.addUser()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Icono de sincronizacion
        syncButton.setOnClickListener {
            if (Sync.isOnline) {
                syncButton.setImageResource(R.drawable.stop_sync)
                Toast.makeText(this, "Sync disabled", Toast.LENGTH_SHORT).show()
            }
            else {
                syncButton.setImageResource(R.drawable.sync_icon )
                Toast.makeText(this, "Sync enabled", Toast.LENGTH_SHORT).show()
            }
            Sync.isOnline = !Sync.isOnline
        }

    }
}
