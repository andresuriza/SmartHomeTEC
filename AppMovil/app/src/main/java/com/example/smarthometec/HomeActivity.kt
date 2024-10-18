package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getStringExtra("username") ?: ""

        binding.welcomeTextView.text = "Hola de nuevo, $currentUser"

        binding.aposentosButton.setOnClickListener {
            val intent = Intent(this, Aposentos::class.java).apply {
                putExtra("username", currentUser)
            }
            startActivity(intent)
        }

        binding.gestionDispositivos.setOnClickListener {
            val intent = Intent(this, AssociateDeviceActivity::class.java).apply {
                putExtra("username", currentUser)
            }
            startActivity(intent)
        }
        binding.transferirDispositivo.setOnClickListener {
            val intent = Intent(this, TransferDeviceActivity::class.java).apply {
                putExtra("username", currentUser)
            }
            startActivity(intent)
        }


        binding.exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}
