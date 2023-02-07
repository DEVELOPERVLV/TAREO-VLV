package com.example.tareo_vlv.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tareo_vlv.R
import com.example.tareo_vlv.database.SedeCRUD
import com.example.tareo_vlv.database.TareadorCRUD
import com.example.tareo_vlv.model.SedeModel
import com.example.tareo_vlv.model.TareadorModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray

class SplashActivity : AppCompatActivity() {

    private lateinit var tacrud: TareadorCRUD
    private lateinit var scrud: SedeCRUD
    var c=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tacrud = TareadorCRUD(this)
        scrud = SedeCRUD(this)

        supportActionBar?.hide()
        CoroutineScope(Dispatchers.IO).launch {

            delay(3000L)
            sedeHTTP()
            tareadorHTTP()
        }
    }

    private fun sedeHTTP(){
        val url = "http://199.241.218.53:60000/tareo/sucursales.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url, {response ->
                try {
                    scrud.deleteSedes()
                    val jsonArray = JSONArray(response)
                    for(i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val idsede = jsonObject.getString("IDSUCURSAL")
                        val sede = jsonObject.getString("DESCRIPCION")
                        val empresa = jsonObject.getString("IDEMPRESA")
                        println("EMPRESA::"+empresa)
                        scrud.insertSede(SedeModel(idsede, sede,empresa))

                    }
                }catch (e:Exception){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            {

            }
        )
        queue.add(stringRequest)
    }

    private fun tareadorHTTP(){
        val url = "http://199.241.218.53:60000/apiTareoEsp/apiTareador.php"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
                try {
                    tacrud.deleteTareador()
                    val jsonArray = JSONArray(response)
                    val cantidad = jsonArray.length()
                    for(i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val DNI = jsonObject.getString("dni")
                        val SEDE = jsonObject.getString("sede")

                        tacrud.insertTareador(TareadorModel(DNI, SEDE))

                        c+=1
                        if (cantidad == c){

                            Toast.makeText(this, "Datos descargados correctamente", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }

                    }


                }catch (e:Exception){

                }
            },

            { error ->

            })
        queue.add(stringRequest)
    }
}