package com.example.tareo_vlv.actividades

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import com.example.tareo_vlv.model.PreModel
import com.example.tareo_vlv.recyclerView.AdaptadorCustom
import com.example.tareo_vlv.recyclerView.ClickListener
import com.example.tareo_vlv.recyclerView.LongClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateTareo : AppCompatActivity() {

    private lateinit var listTareo: RecyclerView
    private lateinit var adaptador: AdaptadorCustom
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var fButton: FloatingActionButton

    var listareo: ArrayList<PreModel>? = null
    private var temtareo: ArrayList<PreModel>? = null

    private var crud: TareoCRUD? = null

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO EN LA APLICACION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_update_tareo)


        listTareo = findViewById(R.id.listTareo)
        fButton = findViewById(R.id.fButton)
        listTareo.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        listTareo.layoutManager = layoutManager

        crud = TareoCRUD(this)

        listareo = crud?.getAllPre()
        temtareo?.addAll(listareo!!)

        fButton.setOnClickListener {

            startActivity(Intent(this, PreRegister::class.java))

        }

        adaptador = AdaptadorCustom(listareo!!, object: ClickListener {
            override fun onClick(view: View, index: Int) {
                val intent = Intent(applicationContext, Update::class.java)
                intent.putExtra("idPre", listareo!![index].id.toString())
                startActivity(intent)
                finish()
            }

        },
            object : LongClickListener {

            override fun longClick(view: View, index: Int) {
                val v = View.inflate(this@UpdateTareo, R.layout.dialog_view, null)
                val builder = AlertDialog.Builder(this@UpdateTareo)
                val nombreAlerta = v.findViewById<TextView>(R.id.nameA)
                val jobAlerta = v.findViewById<TextView>(R.id.jobA)
                nombreAlerta.text = listareo!![index].nombre.toString()
                jobAlerta.text = listareo!![index].job.toString()
                val btnEliminar = v.findViewById<Button>(R.id.btnELimina)
                builder.setView(v)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                btnEliminar.setOnClickListener {
                    crud?.dropAllTareo(listareo!![index].id.toString().toInt())
                    dialog.dismiss()
                    val intent = Intent(applicationContext, UpdateTareo::class.java)
                    startActivity(intent)
                }
            }
        })

        listTareo.adapter = adaptador

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == 4){
            val intent = Intent(applicationContext, PreRegister::class.java)
            startActivity(intent)
        }
        return super.onKeyDown(keyCode, event)
    }


}