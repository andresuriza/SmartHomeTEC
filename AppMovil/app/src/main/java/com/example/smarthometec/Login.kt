package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityLoginBinding
import android.database.sqlite.SQLiteDatabase
import com.example.samrthometec.DatabaseManager

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //  private lateinit var db: SQLiteDatabase  // Base de datos SQLite
    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el binding para el layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar la base de datos
        databaseManager = DatabaseManager(this)
        //db = databaseManager.readableDatabase
        if (!databaseManager.checkDatabase()) {
            Toast.makeText(this, "Database not found!", Toast.LENGTH_SHORT).show()
            return
        }
        // Referencias a los elementos de la interfaz
        val usernameEditText: EditText = binding.username
        val passwordEditText: EditText = binding.password
        val loginButton: Button = binding.loginButton


        // Configuración del botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validar credenciales con la base de datos
            if (databaseManager.checkUser(username, password)) {
                // Mostrar un mensaje de éxito y redirigir
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, InfoActivity::class.java)
                intent.putExtra("username", username)
                // Aquí puedes redirigir a otra actividad si el login es exitoso
                // startActivity(Intent(this, HomeActivity::class.java))
                startActivity(intent)
            } else {
                // Mostrar un mensaje de error
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

