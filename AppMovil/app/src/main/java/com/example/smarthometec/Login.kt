//package com.example.samrthometec
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.example.samrthometec.databinding.ActivityLoginBinding
//import android.database.sqlite.SQLiteDatabase
//import com.example.smarthometec.DatabaseManager
//
//class Login : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var db: SQLiteDatabase  // Base de datos SQLite
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Configurar el binding para el layout
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Inicializar la base de datos
//        val databaseManager = DatabaseManager(this)
//        db = databaseManager.openDatabase()
//
//        // Referencias a los elementos de la interfaz
//        val usernameEditText: EditText = binding.username
//        val passwordEditText: EditText = binding.password
//        val loginButton: Button = binding.loginButton
//        val registerButton: Button = binding.registerButton
//
//        // Configuración del botón de inicio de sesión
//        loginButton.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            // Validar credenciales con la base de datos
//            if (checkUser(username, password)) {
//                // Mostrar un mensaje de éxito y redirigir
//                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
//                // Aquí puedes redirigir a otra actividad si el login es exitoso
//                // startActivity(Intent(this, HomeActivity::class.java))
//            } else {
//                // Mostrar un mensaje de error
//                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Botón para ir a la pantalla de registro
//        registerButton.setOnClickListener {
//            val intent = Intent(this, Registro::class.java)
//            startActivity(intent)
//        }
//    }
//
//    // Método para verificar si un usuario existe en la base de datos
//    private fun checkUser(username: String, password: String): Boolean {
//        val cursor = db.rawQuery(
//            "SELECT * FROM Cliente WHERE username = ? AND password = ?",
//            arrayOf(username, password)
//        )
//
//        val exists = cursor.count > 0
//        cursor.close()
//        return exists
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        db.close()
//    }
//}
//
package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el binding para el layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referencias a los elementos de la interfaz
        val usernameEditText: EditText = binding.username
        val passwordEditText: EditText = binding.password
        val loginButton: Button = binding.loginButton
        val registerButton : Button = binding.registerButton

        // Configuración del botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Lógica simple de validación de credenciales
            if (username == "admin" && password == "1234") {
                // Mostrar un mensaje de éxito
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                // Mostrar un mensaje de error
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        registerButton.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }
}