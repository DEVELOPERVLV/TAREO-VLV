package com.example.tareo_vlv.actividades

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isEmpty
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import com.example.tareo_vlv.database.*
import com.example.tareo_vlv.model.PreModel
import com.example.tareo_vlv.model.TrabajadorModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.*


class PreRegister : AppCompatActivity() {

    private lateinit var cultivo : Spinner
    private lateinit var costcenter: Spinner
    private lateinit var activity: Spinner
    private lateinit var job: Spinner
    private lateinit var dniT: TextInputEditText
    private lateinit var advanceT: TextInputEditText
    private lateinit var timeI : Button
    private lateinit var horaInicio: TextInputLayout
    private lateinit var hourIni: TextInputEditText
    private lateinit var timeF : Button
    private lateinit var hourFin : TextInputEditText
    private lateinit var timeE: Button
    private lateinit var hourE: TextInputEditText
    private lateinit var btnPre: Button
    private lateinit var btnScanner: Button

    private lateinit var crud: TareoCRUD
    private lateinit var culcrud: CultivoCRUD
    private lateinit var costcrud: CcenterCRUD
    private lateinit var acrud: ActiviyCRUD
    private lateinit var lcrud: LaborCRUD
    private lateinit var dbhelper: SQLiteOpenHelper
    private lateinit var tcrud: TrabajadorCRUD
    private lateinit var tarcrud: TareadorCRUD
    private lateinit var rendimiento: TextView
    private lateinit var cantidad: TextView

    var toolbar:Toolbar? = null

    private var inicioLabor: Double = 0.00
    private var finLabor: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO EN LA APLICACION
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_pre_register)
        supportActionBar?.hide()

        culcrud = CultivoCRUD(this)
        costcrud = CcenterCRUD(this)
        acrud = ActiviyCRUD(this)
        lcrud = LaborCRUD(this)
        tcrud = TrabajadorCRUD(this)
        tarcrud = TareadorCRUD(this)

        initView()
        initSpinner(this)
        initToolbar()
        blockSpinner()
        startTime()
        endTime()

        addFood()

        crud = TareoCRUD(this)
        dbhelper = SQLiteOpenHelper(this)

        btnPre.setOnClickListener {
            if(costcenter.equals("")){
                Toast.makeText(this, "Descargue/Actualice datos", Toast.LENGTH_SHORT).show()
            }else{
                addPre()
            }

        }

        btnScanner.setOnClickListener { initScanner() }

    }

    fun initToolbar(){

        toolbar = findViewById(R.id.my_toolbar)
        toolbar?.setLogo(R.mipmap.ic_principal_icon_fondo_round)
        supportActionBar?.setDisplayUseLogoEnabled(false)
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_principal, menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{

        when(item.itemId){

            R.id.icon_register -> {
                val intent = Intent(this, Register()::class.java)
                startActivity(intent)
                return true

            }

            R.id.icon_sync -> {
                val intent = Intent(this, SyncSuccess()::class.java)
                startActivity(intent)
                return true

            }
            R.id.icon_view -> {
                val intent = Intent(this, UpdateTareo()::class.java)
                startActivity(intent)
                return true

            }

            R.id.icon_logout -> {

                MaterialAlertDialogBuilder(this)
                    .setTitle("Alerta")
                    .setMessage("¿Desea cerrar Sesión?")
                    .setNegativeButton("No") { dialog, which -> showSnackBar("Cancelado") }
                    .setPositiveButton("Si") { dialog, which ->
                        showSnackBar("Sesión cerrada")
                        logout()
                    }
                    .show()

                return true
            }

            else -> {return super.onOptionsItemSelected(item)}

        }

    }

    private fun initView(){

        cultivo = findViewById(R.id.cultivo)
        costcenter = findViewById(R.id.costProd)
        activity = findViewById(R.id.activity)
        timeI = findViewById(R.id.timeI)
        hourIni = findViewById(R.id.hourIni)
        horaInicio = findViewById(R.id.horaInicio)
        timeF = findViewById(R.id.timeF)
        hourFin = findViewById(R.id.hourFin)
        timeE = findViewById(R.id.timeE)
        hourE = findViewById(R.id.hourE)
        job = findViewById(R.id.job)
        dniT = findViewById(R.id.dniT)
        advanceT = findViewById(R.id.advanceT)
        btnPre = findViewById(R.id.btnPre)
        btnScanner = findViewById(R.id.btnScanner)
        rendimiento = findViewById(R.id.rendimiento)
        cantidad = findViewById(R.id.cantidad)

    }

    private fun initSpinner(context: Context) {

        arrayListOf("No se encuentra ningún valor")
        val sharedPref = getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()

        val cultivos = culcrud.selectCultivo(userRegistra)
        val listCultivo = arrayListOf<String>()
        for(i in 0 until cultivos.size){

            listCultivo.add(cultivos[i].descripcion.toString())

        }

        val adaptadorcul = ArrayAdapter(context, R.layout.spinner_list, listCultivo)
        cultivo.adapter = adaptadorcul

        cultivo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idcultivo = cultivos[p2].idCultivo.toString()
                println("area:::"+idcultivo)
                initSpinnerCCostos(idcultivo)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    private fun initSpinnerCCostos(area: String){
        val campana = costcrud.selectCCenter(area)
        val lista = arrayListOf<String>()
        for (i in 0 until campana.size) {

            lista.add(campana[i].idconsumidor.toString())

        }

        val adaptador =
            ArrayAdapter(this, R.layout.spinner_list, lista)
        costcenter.adapter = adaptador

        costcenter.onItemSelectedListener = object:  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val ccostos = campana[p2].idconsumidor.toString()

                initSpinnerFase(ccostos.trim())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }
    }

    private fun initSpinnerFase(ccostos: String){
        val actividad = acrud.selectActivity(ccostos)

        Log.d("Actividades ::", actividad.toString())

        val listaCampania = arrayListOf<String>()

        for (i in 0 until actividad.size) {

            listaCampania.add(actividad[i].idactividad.toString() + " - " + actividad[i].descripcion.toString())

        }

        val adapter = ArrayAdapter(this, R.layout.spinner_list, listaCampania)
        activity.adapter = adapter

        activity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val fase = actividad[p2].idactividad.toString()
                println("FASES:::" + fase)
                val rendimientoAct = actividad[p2].rendimiento.toString()

                initSpinnerJob(rendimientoAct, fase)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        }

    private fun initSpinnerJob(rendimientoAct: String, fase: String){


        val labor = lcrud.selectLabor(fase)
        println("labores"+ labor)

        val listaLabor = arrayListOf<String>()

        for (i in 0 until labor.size) {

            listaLabor.add(labor[i].idlabor.toString() + " - " + labor[i].descripcion.toString())

        }

        val adapterLab = ArrayAdapter( this, R.layout.spinner_list, listaLabor)
        job.adapter = adapterLab

        job.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val seleccionLab = labor[p2].idlabor.toString()
                val cantidades = labor[p2].cantidad.toString()


                if(rendimientoAct == "NO"){
                    rendimiento.text = "TRADICIONAL"
                }else{
                    rendimiento.text = "ESTANDAR"
                }
                cantidad.text = cantidades
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

                TODO("Not yet implemented")

            }


        }
    }

    private fun blockSpinner(){

        val block = findViewById<SwitchMaterial>(R.id.block)
        block?.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                cultivo.isEnabled = false
                costcenter.isEnabled = false
                activity.isEnabled = false
                job.isEnabled = false
            }else{
                cultivo.isEnabled = true
                costcenter.isEnabled = true
                activity.isEnabled = true
                job.isEnabled = true
            }
        }

    }

    private fun initScanner(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result.contents == null){
          Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
          }else{
              dniT.setText(result.contents)

              val documento = result.contents
              val personal = getTrabajador(documento)
              if(personal){
                  val personalR: TrabajadorModel? = tcrud.selectPersonal(documento)
                  val nombre = personalR?.trabajador.toString()
                  if(nombre != "null")
                  Toast.makeText(this, personalR?.trabajador.toString(), Toast.LENGTH_SHORT).show()
              }else{
                  Toast.makeText(this, documento, Toast.LENGTH_SHORT).show()
              }

          }
    }


    private fun startTime(){
        timeI.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECCIONA HORA DE INICIO")
                .setHour(6)
                .setMinute(30)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "PreRegister")
            materialTimePicker.addOnPositiveButtonClickListener{
                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                val formattedTimeI: String = when {
                    pickedHour > 12 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    pickedHour == 12 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    pickedHour == 0 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    else -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                }
                inicioLabor = calculationHour(pickedHour, pickedMinute)
                hourIni.setText(formattedTimeI)
            }
        }
    }

    private fun endTime(){
        timeF.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECCIONA HORA DE FIN")
                .setHour(17)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(supportFragmentManager, "PreRegister")
            materialTimePicker.addOnPositiveButtonClickListener {
                val pickedHour: Int = materialTimePicker.hour
                val pickedMinute: Int = materialTimePicker.minute

                val formattedTimeF: String = when {
                    pickedHour > 12 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    pickedHour == 12 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    pickedHour == 0 -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                    else -> {
                        if(pickedMinute < 10){
                            "${materialTimePicker.hour}.0${materialTimePicker.minute}"
                        }else{
                            "${materialTimePicker.hour}.${materialTimePicker.minute}"
                        }
                    }
                }
                finLabor = calculationHour(pickedHour, pickedMinute)
                hourFin.setText(formattedTimeF)
            }
        }
    }

    private fun addFood(){
        var foodDiscount = true

        timeE.setOnClickListener {
            if(foodDiscount) {

                hourE.setText(R.string.foodAc)

                timeE.setBackgroundColor(Color.parseColor("#912A84"))
                timeE.setTextColor(Color.parseColor("#FFFFFFFF"))
                foodDiscount = false

            }else{

                hourE.setText(R.string.foodN)
                foodDiscount = true

                timeE.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }
        }
    }

    private fun calculationHour(hour: Int, minute: Int): Double {

        val newMinute: Double = minute.toDouble() / 60.00
        return hour + newMinute

    }


    private fun addPre(){

        val sharedPref = getSharedPreferences("password",Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()
        val dni = dniT.text.toString()
        val activity = activity.selectedItem.toString()
        val phase = activity.substring(0,3)
        val ccostos = costcenter.selectedItem.toString()
        val job = job.selectedItem.toString()
        //val labor = job.substring(0,6)
        val advance = advanceT.text.toString()
        val timeI = hourIni.text.toString()
        val timeF = hourFin.text.toString()
        val timeE = hourE.text.toString()
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val strDate: String = sdf.format(cal.time)
        val shf = SimpleDateFormat("HH:mm:ss")
        val strHour: String = shf.format(cal.time)
        val estado = 1
        val totales = validaHora(inicioLabor, finLabor, timeE.toDouble())
        val savedSede = sharedPref.getString("SEDE_KEY", null)
        val sedeTareo = savedSede.toString()

        if(dni.isEmpty() || timeI.isEmpty() || timeF.isEmpty()){

            Toast.makeText(this, "No debe de tener DNI, Fecha Inicio y Fin Vacío", Toast.LENGTH_SHORT).show()

        }else{

            try{
                    val trabajador = getTrabajador(dni)
                        if(trabajador){
                            val personalR: TrabajadorModel? = tcrud.selectPersonal(dni)
                            val nombre = personalR?.trabajador.toString()
                            Log.d("error ::", nombre)
                            if(timeF.trim().toFloat() < timeI.trim().toFloat()){
                                Toast.makeText(this, "La hora de inicio no puede ser menor a la hora de fin, registre en formato 24 horas", Toast.LENGTH_LONG).show()
                                clearEditText()

                            }else {

                                if(nombre == "null"){

                                    Toast.makeText(this, "Pre registro agregado trabajador " + dni + " FALTA REGISTRAR", Toast.LENGTH_SHORT).show()
                                    val status = crud.insertPre(PreModel(
                                        id = null,user = userRegistra.trim(), costcenter = ccostos, activity = phase,
                                        job = job, advance = advance.trim(), timeI = timeI.trim(), timeF = timeF.trim(), timeE = timeE.trim(),
                                        dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = "Usuario no registrado o valide DNI", totales = totales.toFloat(), sede = sedeTareo, oprod = ""))

                                    inicioLabor+=0.00
                                    finLabor+=0.00
                                    if(status > -1){

                                        clearEditText()

                                    }else{
                                        Toast.makeText(this, "No se ha registrado", Toast.LENGTH_SHORT).show()
                                    }

                                }else{

                                    Toast.makeText(this, "Pre registro agregado trabajador: " + nombre, Toast.LENGTH_SHORT).show()

                                    val status = crud.insertPre(PreModel(
                                        id = null,user = userRegistra.trim(), costcenter = ccostos, activity = phase,
                                        job = job, advance = advance.trim(), timeI = timeI.trim(), timeF = timeF.trim(), timeE = timeE.trim(),
                                        dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = nombre, totales = totales.toFloat(), sede = sedeTareo, oprod = ""))

                                    inicioLabor+=0.00
                                    finLabor+=0.00

                                    if(status > -1){

                                        clearEditText()

                                    }else{
                                        Toast.makeText(this, "No se ha registrado", Toast.LENGTH_SHORT).show()
                                    }

                                }
                            }


                        }else{
                            Toast.makeText(this, "Trabajador no encontrado en Nisira", Toast.LENGTH_SHORT).show()
                        }

                }catch (e: SQLiteException){
                e.printStackTrace()

            }

        }

    }

    fun clearEditText(){

        dniT.setText("")
        advanceT.setText("")
        timeE.text = ""

    }

    fun getTrabajador(dni: String):Boolean{

        val personalR: TrabajadorModel? = tcrud.selectPersonal(dni)
        return personalR?.equals(null) != true

    }

    fun logout(){
        val sharedPref = getSharedPreferences("password",Context.MODE_PRIVATE)
        val savedString = sharedPref.edit().remove("STRING_KEY").commit()
        Log.d("Preferences::",savedString.toString())
        if (savedString){
            val intent = Intent(this, MainActivity()::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validaHora(horaI: Double, horaF: Double, dscto: Double): Double{
        var dsctos = 0.00

        dsctos += if (dscto == 0.00){
            0.00
        }else{
            dscto / 0.60
        }
        return (horaF - horaI) - (dsctos)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == 4){
            val intent = Intent(this, Principal()::class.java)
            startActivity(intent)
            finish()
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun showSnackBar(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}