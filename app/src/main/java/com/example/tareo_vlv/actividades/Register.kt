package com.example.tareo_vlv.actividades


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tareo_vlv.Confirmation_Send
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import kotlin.collections.HashMap


class Register : AppCompatActivity() {

    private lateinit var cantidad: TextView
    private lateinit var crud: TareoCRUD

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO EN LA APLICACION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_register)

        crud = TareoCRUD(this)
        initView()
        val btnSend = findViewById<Button>(R.id.btnSend)
                btnSend.setOnClickListener {
                    val data = crud.getAllPre()
                    val jsonArray =  JSONArray()
                    for(i in 0 until data.size ){
                        val jsonObjectTareo = JSONObject()
                        try {

                            jsonObjectTareo.put("id", data[i].id)
                            jsonObjectTareo.put("user", data[i].user)
                            jsonObjectTareo.put("costcenter", data[i].costcenter)
                            jsonObjectTareo.put("activity", data[i].activity)
                            jsonObjectTareo.put("job", data[i].job)
                            jsonObjectTareo.put("advance", data[i].advance)
                            jsonObjectTareo.put("timeI", data[i].timeI)
                            jsonObjectTareo.put("timeF", data[i].timeF)
                            jsonObjectTareo.put("timeE", data[i].timeE)
                            jsonObjectTareo.put("dni", data[i].dni)
                            jsonObjectTareo.put("dia", data[i].dia)
                            jsonObjectTareo.put("hora" , data[i].hora)
                            jsonObjectTareo.put("estado", data[i].estado)
                            jsonObjectTareo.put("totales", data[i].totales)
                            jsonObjectTareo.put("sede", data[i].sede)

                        }catch (e: JSONException){
                            e.printStackTrace()
                        }
                        jsonArray.put(jsonObjectTareo)
                    }
                    val json = JSONObject()
                    try {
                        json.put("Tareo", jsonArray)
                    }catch (e: JSONException){
                        e.printStackTrace()
                    }
                    val jsonStr: String = json.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        sendData(jsonStr)

                    }
                }
            }


    private fun initView(){
        cantidad = findViewById(R.id.cantidad)

        val cantidadR = crud.getAllTareo()

        val registros = cantidadR.toString().toInt()

        if(registros > 0){
            cantidad.text = "Tiene $registros registro por subir"
        }else{
            cantidad.text = "No tiene registros por subir"

        }
    }

    private fun sendData(json: String){
        val requestQueue = Volley.newRequestQueue(this)
        val url = "http://199.241.218.53:60000/apiTareoEsp/gTareo.php"
        val solicitud = object: StringRequest(Method.POST, url, Response.Listener {
            response ->
            try {
                Toast.makeText(this, "Registro: $response", Toast.LENGTH_SHORT).show()
                val data = crud.getAllPre()

                if(response.equals("ENVIO COMPLETO")){

                    for (i in 0 until data.size){

                        crud.dropAllTareo(

                            data[i].id.toString().toInt()

                        )
                    }
                    val intent = Intent(this, Confirmation_Send::class.java)
                    startActivity(intent)
                    finish()
                }

                
            }catch (e: JSONException){
                Toast.makeText(applicationContext, "response,$response", Toast.LENGTH_LONG).show()
                e.printStackTrace()

            }

        }, Response.ErrorListener {

            Toast.makeText(applicationContext, "DEBE DE CONECTARSE A INTERNET", Toast.LENGTH_SHORT).show()

        })

        {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["json"] = json
                return params
            }
        }
        requestQueue.add(solicitud)
        solicitud.retryPolicy = DefaultRetryPolicy(
        0,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == 4){
            val intent = Intent(applicationContext, PreRegister::class.java)
            startActivity(intent)
        }
        return super.onKeyDown(keyCode, event)
    }

}