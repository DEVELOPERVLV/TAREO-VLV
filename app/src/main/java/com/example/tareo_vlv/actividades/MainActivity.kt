package com.example.tareo_vlv.actividades

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.tareo_vlv.R
import com.example.tareo_vlv.database.SedeCRUD
import com.example.tareo_vlv.database.TareadorCRUD
import com.example.tareo_vlv.model.TareadorModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var password: TextInputLayout
    private lateinit var ePassword: TextInputEditText

    private lateinit var sede: TextInputLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var bLog: Button

    private lateinit var tacrud: TareadorCRUD
    private lateinit var scrud: SedeCRUD

    var idSedes = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO EN LA APLICACION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_main)
        tacrud = TareadorCRUD(this)
        scrud = SedeCRUD(this)
        //var docu:String = "72768326"
        initView()
        initSpinner(this)

        val sharedPref = getSharedPreferences("password",Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY", null)
        //println("Password ::"+savedString)



        if(savedString == null){

            bLog.setOnClickListener { login() }
        }else{
            if(savedString.isNotEmpty()){
                val intent = Intent(this, PreRegister::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun initView(){
        sede = findViewById(R.id.sede)
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        password = findViewById(R.id.password)
        ePassword = findViewById(R.id.contrasenia)
        bLog = findViewById(R.id.bLog)
    }

    private fun initSpinner(context: Context){

        val sede = scrud.selectSede("001")
        val listaSede = arrayListOf<String>()


        for (i in 0 until sede.size){

            listaSede.add(sede[i].sede.toString())
            //println(sede[i].empresa.toString())
        }
        val adapterLab = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, listaSede)
        autoCompleteTextView.setAdapter(adapterLab)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ ->
                idSedes = sede[p2].idSede.toString()
            }
    }

    private fun saveSharedPreferences(password: String, sede:String){
        val sharedPref = getSharedPreferences("password", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply{
            putString("STRING_KEY", password)
            putString("SEDE_KEY", sede)
        }.apply()
    }

    private fun login(){
        val password = ePassword.text.toString().trim()
        //val sede = autoCompleteTextView.text.toString().trim()

        try {
            val tareador = getTarear(password, idSedes)

            if(tareador){
                val dni = tacrud.getTareador(password, idSedes)
                val dniL = dni?.dni.toString().trim()
                if (password == dniL) {


                    saveSharedPreferences(dniL, idSedes)
                    val intent = Intent(this, PreRegister::class.java)
                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(this,"Contraseña incorrecta o no existe el usuario", Toast.LENGTH_SHORT).show()

                }
            }else{

                Toast.makeText(this,"Solicite Ingreso de Usuario", Toast.LENGTH_SHORT).show()

            }
        }catch (e: SQLiteException){

            e.printStackTrace()

        }

    }

    private fun getTarear(password: String, sede: String):Boolean{

        val personalR: TareadorModel? = tacrud.getTareador(password, sede)
        return personalR?.equals(null) != true

    }

}