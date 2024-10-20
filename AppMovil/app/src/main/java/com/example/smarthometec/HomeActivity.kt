package com.example.samrthometec

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.samrthometec.databinding.ActivityHomeBinding
import com.example.smarthometec.Sync

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
            val intent = Intent(this, DeviceControlActivity::class.java).apply {
                putExtra("username", currentUser)
            }
            startActivity(intent)
        }

        if (Sync.isOnline) {
            binding.imageButton.setImageResource(R.drawable.sync_icon)
        }
        else {
            binding.imageButton.setImageResource(R.drawable.stop_sync )
        }

        binding.imageButton.setOnClickListener {
            if (Sync.isOnline) {
                binding.imageButton.setImageResource(R.drawable.stop_sync)
                Toast.makeText(this, "Sync disabled", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.imageButton.setImageResource(R.drawable.sync_icon )
                Toast.makeText(this, "Sync enabled", Toast.LENGTH_SHORT).show()
            }
            Sync.isOnline = !Sync.isOnline
        }

//        syncButton.setOnClickListener {
////            if (Sync.isOnline) {
////                syncButton.setImageResource(R.drawable.stop_sync)
//                Toast.makeText(this, "Sync disabled", Toast.LENGTH_SHORT).show()
////            }
////            else {
////                syncButton.setImageResource(R.drawable.sync_icon )
////                Toast.makeText(this, "Sync enabled", Toast.LENGTH_SHORT).show()
////            }
////            Sync.isOnline = !Sync.isOnline
//        }
    }
}
