package com.example.tareo_vlv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.tareo_vlv.actividades.PreRegister
import com.example.tareo_vlv.actividades.tareoSOP

class DashBoard : AppCompatActivity() {

    private lateinit var conOP: CardView
    private lateinit var sinOP: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        initView()

    }

    private fun initView(){

        conOP = findViewById(R.id.conOP)
        sinOP = findViewById(R.id.sinOP)

    }

    private fun sinOp(){
        sinOP.setOnClickListener{

            val intent = Intent(this, tareoSOP::class.java)
            startActivity(intent)

        }
    }

    private fun conOp(){
        conOP.setOnClickListener{

            val intent = Intent(this, PreRegister::class.java)
            startActivity(intent)

        }
    }
}