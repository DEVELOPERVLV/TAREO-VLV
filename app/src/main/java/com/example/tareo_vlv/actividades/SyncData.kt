package com.example.tareo_vlv.actividades

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tareo_vlv.R
import com.example.tareo_vlv.database.*
import com.example.tareo_vlv.model.*
import kotlinx.coroutines.*
import org.json.JSONArray
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis


class SyncData : Fragment() {

    private lateinit var tcrud: TrabajadorCRUD
    private lateinit var costcrud: CcenterCRUD
    private lateinit var acrud: ActiviyCRUD
    private lateinit var lcrud: LaborCRUD
    private lateinit var tacrud: TareadorCRUD
    private lateinit var culcrud: CultivoCRUD
    private lateinit var opcrud: OProdCRUD
    private lateinit var descargas: ProgressBar
    private lateinit var downloadData: TextView

    private var e = 0
    private var a = 0
    private var b = 0
    private var c = 0
    private var d = 0
    private var f = 0
    private var g = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_sync_data, container, false)
        val context: Context = requireContext()
        descargas = view.findViewById(R.id.Descargas)
        downloadData = view.findViewById(R.id.downloadData)
        tcrud = TrabajadorCRUD(context)
        tacrud = TareadorCRUD(context)
        culcrud = CultivoCRUD(context)
        costcrud = CcenterCRUD(context)
        acrud = ActiviyCRUD(context)
        lcrud = LaborCRUD(context)
        opcrud = OProdCRUD(context)

        updateData(view, context)

        return view
    }

    private fun updateData(view: View, context: Context){
        val btnUpd = view.findViewById<Button>(R.id.btnUpd)
        val btnUpdCultivo = view.findViewById<Button>(R.id.btnUpdCultivo)
        val btnUpdCC = view.findViewById<Button>(R.id.btnUpdCCostos)
        val btnUpdOP = view.findViewById<Button>(R.id.btnUpdOP)
        val btnUpdAC = view.findViewById<Button>(R.id.btnUpdActividad)
        val btnUpdLA = view.findViewById<Button>(R.id.btnUpdLabor)

        btnUpd.setOnClickListener {
            a+=1
            CoroutineScope(Dispatchers.IO).launch {

                    tcrud.deleteTrabajador()



                measureTimeMillis {
                    val job1 = launch {
                        delay(500L)
                        trabajadorHTTP()
                    }


                    joinAll(job1)

                }
            }

            if(a==1){

                btnUpd.isEnabled = false

            }
        }

        btnUpdCultivo.setOnClickListener {
            b+=1
            val sharedPref = context.getSharedPreferences("password", Context.MODE_PRIVATE)
            val savedString = sharedPref.getString("STRING_KEY", null)
            val userRegistra = savedString.toString()
            println("USUARIO:::"+userRegistra)
            CoroutineScope(Dispatchers.IO).launch {
                culcrud.delelteCultivo()

                measureTimeMillis {

                    val job1 = launch {
                        delay(1000L)
                        cultivoHTTP(userRegistra)
                    }

                    joinAll(job1)

                }
            }
            println(b)
            if(b==1){
                btnUpdCultivo.isEnabled = false
            }
        }

        btnUpdCC.setOnClickListener {
            c+=1
            costcrud.deleteCcenter()
            val sharedPref = context.getSharedPreferences("password", Context.MODE_PRIVATE)
            val savedString = sharedPref.getString("STRING_KEY", null)
            val userRegistra = savedString.toString()
            CoroutineScope(Dispatchers.IO).launch {
                measureTimeMillis {

                    val job1 = launch {
                        delay(1500L)
                        costosHTTP(userRegistra)
                    }

                    joinAll(job1)

                }
            }
            if(c==1){
                btnUpdCC.isEnabled = false
            }
        }


        btnUpdOP.setOnClickListener {
            d+=1
            opcrud.deleteOP()
            CoroutineScope(Dispatchers.IO).launch {
                measureTimeMillis {

                    val job1 = launch {
                        delay(500L)
                        oprodHTTP()

                    }
                    joinAll(job1)


                }
            }
            if(d==1){
                btnUpdOP.isEnabled = false
            }
        }

        btnUpdAC.setOnClickListener {
            f+=1
            acrud.deleteActivity()
            CoroutineScope(Dispatchers.IO).launch {
                measureTimeMillis {

                    val job1 = launch {
                        delay(500L)
                        activityHTTP()
                    }

                    joinAll(job1)


                }
            }
            if(f==1){
                btnUpdAC.isEnabled = false
            }
        }

        btnUpdLA.setOnClickListener {
            g+=1
            lcrud.deleteLabor()
            CoroutineScope(Dispatchers.IO).launch {
                measureTimeMillis {

                    val job1 = launch {
                        delay(500L)
                        laboresHTTP()
                    }

                    joinAll(job1)

                }
            }
            if(g==1){
                btnUpdLA.isEnabled = false
            }
        }
    }

    private fun trabajadorHTTP(){
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET, "http://199.241.218.53:60000/tareo/personal_tareo.php",
        { response ->
            insertTrabajador(response)
        },

        { error ->

            error.message?.let {
                Log.d("HTTP_REQUEST", it)
            }

        })

    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertTrabajador(response: String){

        try {

            val jsonArray = JSONArray(response)
            val cantidades = jsonArray.length()

            for(i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val idcodigogeneral = jsonObject.getString("IDCODIGOGENERAL")
                val trabajador = jsonObject.getString("TRABAJADOR")

                tcrud.insertPersonal(TrabajadorModel(idcodigogeneral, trabajador))

                thread {

                    e+=1

                    if (cantidades == e){
                        redirection()
                    }

                }

            }
        }catch (e:Exception){

        }
    }

private fun cultivoHTTP(dni: String){

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(

        Request.Method.GET, "http://199.241.218.53:60000/apiTareoEsp/apiAreaLC.php?funcion=obtenerCultivos&dni=$dni",
        { response ->
            insertCLA(response)
            print("Cultivo "+response)
        },
        {

        })
    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertCLA(response: String){
    var cont = 0
    try {

        val jsonArray = JSONArray(response)
        val cantidades = jsonArray.length()


        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            culcrud.insertCultivo(
                CultivoModel(
                    jsonObject.getString("idAreaCultivo")
                    ,jsonObject.getString("dni")
                    ,jsonObject.getString("descripcion")
                )
            )

            thread {

                cont+=1

                if (cantidades == cont){
                    redirection()
                }

            }

        }




    }catch (e:Exception){

    }
}

private fun costosHTTP(dni: String){
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(

        Request.Method.GET, "http://199.241.218.53:60000/apiTareoEsp/apiCcostos.php?funcion=obtenerCCostos&dni=$dni", { response ->
            insertCC(response)
            print("Costos "+response)
        },

        {


        })
    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertCC(response: String){
    var cont = 0
    try {
        val jsonArray = JSONArray(response)
        val cantidades = jsonArray.length()

        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            costcrud.insetCCenter(
                CCenterModel(
                    jsonObject.getString("idAreaCultivo")
                    ,jsonObject.getString("ccostos")
                )
            )

            thread {

                cont+=1
                if (cantidades == cont){
                    redirection()
                }


            }

        }

    }catch (e:Exception){

    }
}

private fun oprodHTTP(){
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        "http://199.241.218.53:60000/apiTareoEspTI/apiOprod.php",{
                response ->
            insertOprod(response)
            print("Orden "+response)
        }, {error ->

            error.message?.let { Log.d("HTTP_REQUEST", it)}

        }
    )
    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertOprod(response: String){
    var cont = 0
    try {

        val jsonArray = JSONArray(response)
        val cantidades = jsonArray.length()

        for (i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)

            opcrud.insertOProd(
                OProdModel(
                    jsonObject.getString("IDORDENPRO"),
                    jsonObject.getString("idconsumidor"),
                    jsonObject.getString("IDMANUAL")
                )
            )

            thread {

                cont+=1

                if (cantidades == cont){
                    redirection()
                }


            }

            println("OPS AGREGADAS")

        }


    }catch (e:Exception){

    }
}

private fun activityHTTP(){

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        "http://199.241.218.53:60000/apiTareoEspTI/apiFase.php",
        { response ->
            insertFase(response)
            print("Actividades "+response)
        }, {error ->

            error.message?.let { Log.d("HTTP_REQUEST", it) }

        })
    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertFase(response: String){
    var cont = 0
    try {
        val jsonArray = JSONArray(response)
        val cantidades = jsonArray.length()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            acrud.insertActivity(
                ActivityModel(
                    jsonObject.getString("IDACTIVIDAD")
                    ,jsonObject.getString("DESCRIPCION")
                    ,jsonObject.getString("POR_RENDIMIENTO")
                    ,jsonObject.getString("IDCONSUMIDOR")
                )
            )

            thread {

                cont+=1
                if (cantidades == cont){
                    redirection()
                }

            }

        }

    }catch (e:Exception){

    }
}

private fun laboresHTTP(){

    val queue = Volley.newRequestQueue(context)

    val stringRequest = StringRequest(

        Request.Method.GET,
        "http://199.241.218.53:60000/apiTareoEspTI/apiLabor.php", {
                response ->
            insertLab(response)
            print("Labor "+response)
        },

        { error ->

            error.message?.let { Log.d("HTTP_REQUEST", it) }

        })
    queue.add(stringRequest)
    queue.cache.clear()
}

private fun insertLab(response: String){
    var cont = 0
    try {
        val jsonArray = JSONArray(response)
        val cantidades = jsonArray.length()

        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            lcrud.insertLabor(
                LaborModel(
                    jsonObject.getString("CODIGO")
                    ,jsonObject.getString("idlabor")
                    ,jsonObject.getString("DESCRIPCION")
                    ,jsonObject.getString("cantidad")
                )
            )

            thread {

                cont+=1

                if (cantidades == cont){

                    redirection()

                }


            }

        }


    }catch (e: Exception){

    }

}


private fun redirection(){


    val handler = Handler(Looper.getMainLooper())
    handler.post {

       Toast.makeText(context, "Datos descargados correctamente", Toast.LENGTH_SHORT).show()

    }

/* val intent = Intent(context, Confirmation_Send::class.java )
        startActivity(intent)*/
}

}