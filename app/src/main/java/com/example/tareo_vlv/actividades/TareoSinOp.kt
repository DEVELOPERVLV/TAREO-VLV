package com.example.tareo_vlv.actividades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import com.example.tareo_vlv.database.*
import com.example.tareo_vlv.model.PreModel
import com.example.tareo_vlv.model.TrabajadorModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.*


class TareoSinOp : Fragment() {

    private lateinit var cultivoSOP : Spinner
    private lateinit var costcenterSOP: Spinner
    private lateinit var activitySOP: Spinner
    private lateinit var jobSOP: Spinner
    private lateinit var dniTSOP: TextInputEditText
    private lateinit var advanceTSOP: TextInputEditText
    private lateinit var timeISOP : Button
    private lateinit var horaInicioSOP: TextInputLayout
    private lateinit var hourIniSOP: TextInputEditText
    private lateinit var timeFSOP : Button
    private lateinit var hourFinSOP : TextInputEditText
    private lateinit var timeESOP: Button
    private lateinit var hourESOP: TextInputEditText
    private lateinit var btnPreSOP: Button
    private lateinit var btnScannerSOP: Button

    private lateinit var crud: TareoCRUD
    private lateinit var culcrud: CultivoCRUD
    private lateinit var costcrud: CcenterCRUD
    private lateinit var acrud: ActiviyCRUD
    private lateinit var lcrud: LaborCRUD
    private lateinit var dbhelper: SQLiteOpenHelper
    private lateinit var tcrud: TrabajadorCRUD
    private lateinit var tarcrud: TareadorCRUD
    private lateinit var confirCRUD: comedorCRUD
    private lateinit var rendimiento: TextView
    private lateinit var cantidad: TextView

    var toolbar: Toolbar? = null

    private var inicioLabor: Double = 0.00
    private var finLabor: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_tareo_sin_op, container, false)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val context: Context = requireContext()
        culcrud = CultivoCRUD(context)
        costcrud = CcenterCRUD(context)
        acrud = ActiviyCRUD(context)
        lcrud = LaborCRUD(context)
        tcrud = TrabajadorCRUD(context)
        tarcrud = TareadorCRUD(context)
        confirCRUD = comedorCRUD(context)

        initView(view)
        initSpinner(context)

        blockSpinner(view)
        startTime()
        endTime()

        addFood()

        crud = TareoCRUD(context)
        dbhelper = SQLiteOpenHelper(context)

        btnPreSOP.setOnClickListener {
            if(costcenterSOP.equals("")){
                Toast.makeText(context, "Descargue/Actualice datos", Toast.LENGTH_SHORT).show()
            }else{
                addPre()
            }

        }

        btnScannerSOP.setOnClickListener { initScanner(context) }
        initView(view)

        return view
    }

    private fun initView(view: View){

        cultivoSOP = view.findViewById(R.id.cultivoSOP)
        costcenterSOP = view.findViewById(R.id.costCenterSOP)
        activitySOP = view.findViewById(R.id.activitySOP)
        timeISOP = view.findViewById(R.id.timeISOP)
        hourIniSOP = view.findViewById(R.id.hourIniSOP)
        horaInicioSOP = view.findViewById(R.id.horaInicioSOP)
        timeFSOP = view.findViewById(R.id.timeFSOP)
        hourFinSOP = view.findViewById(R.id.hourFinSOP)
        timeESOP = view.findViewById(R.id.timeESOP)
        hourESOP = view.findViewById(R.id.hourESOP)
        jobSOP = view.findViewById(R.id.jobSOP)
        dniTSOP = view.findViewById(R.id.dniTSOP)
        advanceTSOP = view.findViewById(R.id.advanceTSOP)
        btnPreSOP = view.findViewById(R.id.btnPreSOP)
        btnScannerSOP = view.findViewById(R.id.btnScannerSOP)
        rendimiento = view.findViewById(R.id.rendimientoSOP)
        cantidad = view.findViewById(R.id.cantidadSOP)

    }

    private fun initSpinner(context: Context) {

        arrayListOf("No se encuentra ningún valor")
        val sharedPref = context.getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()

        val cultivos = culcrud.selectCultivo(userRegistra)
        val listCultivo = arrayListOf<String>()
        for(i in 0 until cultivos.size){

            listCultivo.add(cultivos[i].descripcion.toString())

        }

        val adaptadorcul = ArrayAdapter(context, R.layout.spinner_list, listCultivo)
        cultivoSOP.adapter = adaptadorcul

        cultivoSOP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idcultivo = cultivos[p2].idCultivo.toString()
                println("area:::"+idcultivo)
                initSpinnerCCostos(idcultivo, context)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    private fun initSpinnerCCostos(area: String, context: Context){
        val campana = costcrud.selectCCenter(area)
        val lista = arrayListOf<String>()
        for (i in 0 until campana.size) {

            lista.add(campana[i].idconsumidor.toString())

        }

        val adaptador =
            ArrayAdapter(context, R.layout.spinner_list, lista)
        costcenterSOP.adapter = adaptador

        costcenterSOP.onItemSelectedListener = object:  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val ccostos = campana[p2].idconsumidor.toString()

                initSpinnerFase(ccostos.trim(), context)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }
    }

    private fun initSpinnerFase(ccostos: String, context: Context){
        val actividad = acrud.selectActivity(ccostos)

        Log.d("Actividades ::", actividad.toString())

        val listaCampania = arrayListOf<String>()

        for (i in 0 until actividad.size) {

            listaCampania.add(actividad[i].idactividad.toString() + " - " + actividad[i].descripcion.toString())

        }

        val adapter = ArrayAdapter(context, R.layout.spinner_list, listaCampania)
        activitySOP.adapter = adapter

        activitySOP.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val fase = actividad[p2].idactividad.toString()
                println("FASES:::" + fase)
                val rendimientoAct = actividad[p2].rendimiento.toString()

                initSpinnerJob(rendimientoAct, fase, context)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }

    private fun initSpinnerJob(rendimientoAct: String, fase: String, context: Context){


        val labor = lcrud.selectLabor(fase)
        println("labores"+ labor)

        val listaLabor = arrayListOf<String>()

        for (i in 0 until labor.size) {

            listaLabor.add(labor[i].idlabor.toString() + " - " + labor[i].descripcion.toString())

        }

        val adapterLab = ArrayAdapter(context, R.layout.spinner_list, listaLabor)
        jobSOP.adapter = adapterLab

        jobSOP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun blockSpinner(view: View){

        val block = view.findViewById<SwitchMaterial>(R.id.block)
        block?.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                cultivoSOP.isEnabled = false
                costcenterSOP.isEnabled = false
                activitySOP.isEnabled = false
                jobSOP.isEnabled = false
            }else{
                cultivoSOP.isEnabled = true
                costcenterSOP.isEnabled = true
                activitySOP.isEnabled = true
                jobSOP.isEnabled = true
            }
        }

    }

    private fun initScanner(context: Context){
        val integrator = IntentIntegrator(context as Activity?)
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
            Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
        }else{
            dniTSOP.setText(result.contents)

            val documento = result.contents
            val personal = getTrabajador(documento)
            if(personal){
                val personalR: TrabajadorModel? = tcrud.selectPersonal(documento)
                val nombre = personalR?.trabajador.toString()
                if(nombre != "null")
                    Toast.makeText(context, personalR?.trabajador.toString(), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, documento, Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun startTime(){
        timeISOP.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECCIONA HORA DE INICIO")
                .setHour(6)
                .setMinute(30)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(parentFragmentManager, "PreRegister")
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
                hourIniSOP.setText(formattedTimeI)
            }
        }
    }

    private fun endTime(){
        timeFSOP.setOnClickListener {
            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECCIONA HORA DE FIN")
                .setHour(17)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            materialTimePicker.show(parentFragmentManager, "PreRegister")
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
                hourFinSOP.setText(formattedTimeF)
            }
        }
    }

    private fun addFood(){
        var foodDiscount = true

        timeESOP.setOnClickListener {
            val minutos = confirCRUD.selectComedor()

            if(foodDiscount) {

                hourESOP.setText(minutos[0].minutos.toString())

                timeESOP.setBackgroundColor(Color.parseColor("#912A84"))
                timeESOP.setTextColor(Color.parseColor("#FFFFFFFF"))
                foodDiscount = false

            }else{

                hourESOP.setText(R.string.foodN)
                foodDiscount = true

                timeESOP.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }
        }
    }

    private fun calculationHour(hour: Int, minute: Int): Double {

        val newMinute: Double = minute.toDouble() / 60.00
        return hour + newMinute

    }


    private fun addPre(){

        val sharedPref = context?.getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref?.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()
        val dni = dniTSOP.text.toString()
        val activity = activitySOP.selectedItem.toString()
        val phase = activity.substring(0,3)
        val ccostos = costcenterSOP.selectedItem.toString()
        val job = jobSOP.selectedItem.toString()
        //val labor = job.substring(0,6)
        val advance = advanceTSOP.text.toString()
        val timeI = hourIniSOP.text.toString()
        val timeF = hourFinSOP.text.toString()
        val timeE = hourESOP.text.toString()
        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val strDate: String = sdf.format(cal.time)
        val shf = SimpleDateFormat("HH:mm:ss")
        val strHour: String = shf.format(cal.time)
        val estado = 1
        val totales = validaHora(inicioLabor, finLabor, timeE.toDouble())
        val savedSede = sharedPref?.getString("SEDE_KEY", null)
        val sedeTareo = savedSede.toString()

        if(dni.isEmpty() || timeI.isEmpty() || timeF.isEmpty()){

            Toast.makeText(context, "No debe de tener DNI, Fecha Inicio y Fin Vacío", Toast.LENGTH_SHORT).show()

        }else{

            try{
                val trabajador = getTrabajador(dni)
                if(trabajador){
                    val personalR: TrabajadorModel? = tcrud.selectPersonal(dni)
                    val nombre = personalR?.trabajador.toString()
                    Log.d("error ::", nombre)
                    if(timeF.trim().toFloat() < timeI.trim().toFloat()){
                        Toast.makeText(context, "La hora de inicio no puede ser menor a la hora de fin, registre en formato 24 horas", Toast.LENGTH_LONG).show()
                        clearEditText()

                    }else {

                        if(nombre == "null"){

                            Toast.makeText(context, "Pre registro agregado trabajador " + dni + " FALTA REGISTRAR", Toast.LENGTH_SHORT).show()
                            val status = crud.insertPre(
                                PreModel(
                                    id = null,user = userRegistra.trim(), costcenter = ccostos, activity = phase,
                                    job = job, advance = advance.trim(), timeI = timeI.trim(), timeF = timeF.trim(), timeE = timeE.trim(),
                                    dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = "Usuario no registrado o valide DNI", totales = totales.toFloat(), sede = sedeTareo, oprod = "")
                            )

                            inicioLabor+=0.00
                            finLabor+=0.00
                            if(status > -1){

                                clearEditText()

                            }else{
                                Toast.makeText(context, "No se ha registrado", Toast.LENGTH_SHORT).show()
                            }

                        }else{

                            Toast.makeText(context, "Pre registro agregado trabajador: " + nombre, Toast.LENGTH_SHORT).show()

                            val status = crud.insertPre(
                                PreModel(
                                    id = null,user = userRegistra.trim(), costcenter = ccostos, activity = phase,
                                    job = job, advance = advance.trim(), timeI = timeI.trim(), timeF = timeF.trim(), timeE = timeE.trim(),
                                    dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = nombre, totales = totales.toFloat(), sede = sedeTareo, oprod = "")
                            )

                            inicioLabor+=0.00
                            finLabor+=0.00

                            if(status > -1){

                                clearEditText()

                            }else{
                                Toast.makeText(context, "No se ha registrado", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }


                }else{
                    Toast.makeText(context, "Trabajador no encontrado en Nisira", Toast.LENGTH_SHORT).show()
                }

            }catch (e: SQLiteException){
                e.printStackTrace()

            }

        }

    }

    fun clearEditText(){

        dniTSOP.setText("")
        advanceTSOP.setText("")
        timeESOP.text = ""

    }

    fun getTrabajador(dni: String):Boolean{

        val personalR: TrabajadorModel? = tcrud.selectPersonal(dni)
        return personalR?.equals(null) != true

    }

    fun logout(){
        val sharedPref = context?.getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref?.edit()?.remove("STRING_KEY")?.commit()
        Log.d("Preferences::",savedString.toString())
        if (savedString!!){
            val intent = Intent(context, MainActivity()::class.java)
            startActivity(intent)

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



}