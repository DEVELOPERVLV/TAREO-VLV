package com.example.tareo_vlv.actividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tareo_vlv.Confirmation_Send
import com.example.tareo_vlv.Error_Send
import com.example.tareo_vlv.R
import com.example.tareo_vlv.database.*
import com.example.tareo_vlv.model.*
import kotlinx.coroutines.*
import org.json.JSONArray
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


class SyncSuccess : AppCompatActivity() {

    private lateinit var tcrud: TrabajadorCRUD
    private lateinit var costcrud: CcenterCRUD
    private lateinit var acrud: ActiviyCRUD
    private lateinit var lcrud: LaborCRUD
    private lateinit var tacrud: TareadorCRUD
    private lateinit var culcrud: CultivoCRUD

    private var e = 0
    private var a = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO EN EL APP
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_sync_success)

        tcrud = TrabajadorCRUD(this)
        tacrud = TareadorCRUD(this)
        culcrud = CultivoCRUD(this)
        costcrud = CcenterCRUD(this)
        acrud = ActiviyCRUD(this)
        lcrud = LaborCRUD(this)

        updateData()

    }

    private fun updateData(){
        val btnUpd = findViewById<Button>(R.id.btnUpd)

                btnUpd.setOnClickListener {
                    a+=1
                    val sharedPref = getSharedPreferences("password", Context.MODE_PRIVATE)
                    val savedString = sharedPref.getString("STRING_KEY", null)
                    val userRegistra = savedString.toString()

                    CoroutineScope(Dispatchers.IO).launch {
                        tcrud.deleteTrabajador()
                        culcrud.delelteCultivo()
                        costcrud.deleteCcenter()
                        acrud.deleteActivity()
                        lcrud.deleteLabor()
                        measureTimeMillis {
                            val job1 = launch {
                                delay(500L)
                                cultivoHTTP(userRegistra)
                            }
                            val job2 = launch {
                                delay(1000L)
                                costosHTTP(userRegistra)
                            }
                            val job3 = launch {
                                delay(1500L)
                                trabajadorHTTP()
                            }
                            val job4 = launch {
                                delay(2000L)
                                laboresHTTP()
                            }

                            joinAll(job1, job2, job3, job4)

                        }
                    }

                    if(a==1){
                        btnUpd.isEnabled = false
                    }
                }
            }

    private fun trabajadorHTTP(){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, "http://199.241.218.53:60000/tareo/personal_tareo.php",
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for(i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val idcodigogeneral = jsonObject.getString("IDCODIGOGENERAL")
                        val trabajador = jsonObject.getString("TRABAJADOR")

                        tcrud.insertPersonal(TrabajadorModel(idcodigogeneral, trabajador))

                    }

                }catch (e:Exception){

                }
            },

            { error ->

                error.message?.let {
                    Log.d("HTTP_REQUEST", it)
                }

            })

        queue.add(stringRequest)
        queue.cache.clear()
    }

    private fun cultivoHTTP(dni: String){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(

            Request.Method.GET, "http://199.241.218.53:60000/apiTareoEsp/apiAreaLC.php?funcion=obtenerCultivos&dni=$dni",
            { response ->
                insertCLA(response)
            },
            {
                a+=0

                val intent = Intent(this, Error_Send::class.java)
                startActivity(intent)

            })
        queue.add(stringRequest)
        queue.cache.clear()
    }

    private fun insertCLA(response: String){
        try {
            val jsonArray = JSONArray(response)

            thread {

                for(i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    culcrud.insertCultivo(
                        CultivoModel(
                            jsonObject.getString("idAreaCultivo")
                            ,jsonObject.getString("dni")
                            ,jsonObject.getString("descripcion")
                    )
                    )

                    println(jsonObject.getString("descripcion"))

                }

            }.start()

        }catch (e:Exception){

        }
    }

    private fun costosHTTP(dni: String){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(

                Request.Method.GET, "http://199.241.218.53:60000/apiTareoEsp/apiCcostos.php?funcion=obtenerCCostos&dni=$dni", { response ->
                    insertCC(response)
                },

            {


            })
        queue.add(stringRequest)
        queue.cache.clear()
    }

    private fun insertCC(response: String){
        try {
            val jsonArray = JSONArray(response)
            thread {

                for(i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    costcrud.insetCCenter(CCenterModel(
                        jsonObject.getString("idAreaCultivo")
                        ,jsonObject.getString("ccostos")
                    ))

                    activityHTTP(jsonObject.getString("ccostos"))

                }

            }.start()
        }catch (e:Exception){

        }
    }

    private fun activityHTTP(ccostos: String){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "http://199.241.218.53:60000/apiTareoEsp/apiFase.php?funcion=obtenerActividad&ccostos=$ccostos",
            { response ->
                insertFase(response)
            }, {error ->

                error.message?.let { Log.d("HTTP_REQUEST", it) }

            })
        queue.add(stringRequest)
        queue.cache.clear()
    }

    private fun insertFase(response: String){
        try {
            val jsonArray = JSONArray(response)

            thread {

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    acrud.insertActivity(ActivityModel(
                        jsonObject.getString("IDACTIVIDAD")
                        ,jsonObject.getString("DESCRIPCION")
                        ,jsonObject.getString("POR_RENDIMIENTO")
                        ,jsonObject.getString("IDCONSUMIDOR")
                    ))

                }
            }.start()
        }catch (e:Exception){

        }
    }

    private fun laboresHTTP(){

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(

                Request.Method.GET,
            "http://199.241.218.53:60000/apiTareoEsp/apiLabor.php", {
                    response ->
                insertLab(response)

                },

            { error ->

                error.message?.let { Log.d("HTTP_REQUEST", it) }

            })
        queue.add(stringRequest)
        queue.cache.clear()
    }

    private fun insertLab(response: String){

        try {
            val jsonArray = JSONArray(response)
            val cantidades = jsonArray.length()

            thread {

                for(i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    lcrud.insertLabor(LaborModel(
                        jsonObject.getString("IDACTIVIDAD")
                        ,jsonObject.getString("IDLABOR")
                        ,jsonObject.getString("DESCRIPCION")
                        ,jsonObject.getString("cantidad")
                    ))

                    e+=1
                    if(cantidades == e){
                        redirection()
                    }

                }
            }.start()
        }catch (e: Exception){

        }

    }


    private fun redirection(){

        val intent = Intent(this, Confirmation_Send::class.java)
        startActivity(intent)

    }

    }