package com.example.tareo_vlv

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tareo_vlv.actividades.SyncData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Confirmation_Send : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_send)

        supportActionBar?.hide()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)

            startActivity(Intent(this@Confirmation_Send, SyncData::class.java))

        }

    }
}