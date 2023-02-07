package com.example.tareo_vlv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tareo_vlv.actividades.PreRegister
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Error_Send : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_send)

        supportActionBar?.hide()
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500L)
            startActivity(Intent(this@Error_Send, PreRegister::class.java))
            finish()
        }

        Toast.makeText(this, "DEBE CONECTARSE A INTERNET", Toast.LENGTH_SHORT).show()
    }
}