package com.example.tareo_vlv.actividades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
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


class TareoConOP : Fragment() {

    private lateinit var costCenter : Spinner
    private lateinit var oProd: Spinner
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
    private lateinit var oProdCRUD: OProdCRUD
    private lateinit var rendimiento: TextView
    private lateinit var cantidad: TextView

    var toolbar: Toolbar? = null

    private var inicioLabor: Double = 0.00
    private var finLabor: Double = 0.00

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_tareo_con_o_p, container, false)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val context: Context = requireContext()
        culcrud = CultivoCRUD(context)
        costcrud = CcenterCRUD(context)
        acrud = ActiviyCRUD(context)
        lcrud = LaborCRUD(context)
        tcrud = TrabajadorCRUD(context)
        tarcrud = TareadorCRUD(context)
        oProdCRUD = OProdCRUD(context)

        initView(view)
        initSpinner(context)

        blockSpinner(view)
        startTime()
        endTime()

        addFood()

        crud = TareoCRUD(context)
        dbhelper = SQLiteOpenHelper(context)

        btnPre.setOnClickListener {
            if(costCenter.equals("")){
                Toast.makeText(context, "Descargue/Actualice datos", Toast.LENGTH_SHORT).show()
            }else{
                addPre()
            }

        }

        btnScanner.setOnClickListener { initScanner(context) }
        initView(view)

        return view
    }

    private fun initView(view: View){

        costCenter = view.findViewById(R.id.costCenter)
        oProd = view.findViewById(R.id.oProd)
        activity = view.findViewById(R.id.activity)
        timeI = view.findViewById(R.id.timeI)
        hourIni = view.findViewById(R.id.hourIni)
        horaInicio = view.findViewById(R.id.horaInicio)
        timeF = view.findViewById(R.id.timeF)
        hourFin = view.findViewById(R.id.hourFin)
        timeE = view.findViewById(R.id.timeE)
        hourE = view.findViewById(R.id.hourE)
        job = view.findViewById(R.id.job)
        dniT = view.findViewById(R.id.dniT)
        advanceT = view.findViewById(R.id.advanceT)
        btnPre = view.findViewById(R.id.btnPre)
        btnScanner = view.findViewById(R.id.btnScanner)
        rendimiento = view.findViewById(R.id.rendimiento)
        cantidad = view.findViewById(R.id.cantidad)

    }

    private fun initSpinner(context: Context) {

        arrayListOf("No se encuentra ningún valor")
        val sharedPref = context.getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()

        val costCenters = costcrud.selectCCenter(userRegistra)
        val listCultivo = arrayListOf<String>()
        for(i in 0 until costCenters.size){

            listCultivo.add(costCenters[i].idconsumidor.toString())

        }

        val adaptadorcul = ArrayAdapter(context, R.layout.spinner_list, listCultivo)
        costCenter.adapter = adaptadorcul

        costCenter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val idconsumidores = costCenters[p2].idconsumidor.toString()
                println("Centro de Costos:::"+idconsumidores)
                initSpinnerOProd(idconsumidores, context)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    private fun initSpinnerOProd(idconsumidores: String, context: Context){
        val oProds = oProdCRUD.selectOP(idconsumidores)
        val lista = arrayListOf<String>()
        for (i in 0 until oProds.size) {

            lista.add(oProds[i].idmanual.toString())

        }

        val adaptador =
            ArrayAdapter(context, R.layout.spinner_list, lista)
        oProd.adapter = adaptador

        oProd.onItemSelectedListener = object:  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val ccostos = oProds[p2].idconsumidor.toString()

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
        activity.adapter = adapter

        activity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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

    private fun blockSpinner(view: View){

        val block = view.findViewById<SwitchMaterial>(R.id.block)
        block?.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                costCenter.isEnabled = false
                oProd.isEnabled = false
                activity.isEnabled = false
                job.isEnabled = false
            }else{
                costCenter.isEnabled = true
                oProd.isEnabled = true
                activity.isEnabled = true
                job.isEnabled = true
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
            dniT.setText(result.contents)

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
        timeI.setOnClickListener {
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

        val sharedPref = context?.getSharedPreferences("password", Context.MODE_PRIVATE)
        val savedString = sharedPref?.getString("STRING_KEY", null)
        val userRegistra = savedString.toString()
        val dni = dniT.text.toString()
        val activity = activity.selectedItem.toString()
        val phase = activity.substring(0,3)
        val ccostos = costCenter.selectedItem.toString()
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
        val savedSede = sharedPref?.getString("SEDE_KEY", null)
        val sedeTareo = savedSede.toString()
        val oprod = oProd.selectedItem.toString()

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
                                dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = "Usuario no registrado o valide DNI",
                                    totales = totales.toFloat(), sede = sedeTareo, oprod = oprod)
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
                                dni = dni.trim(), dia = strDate, hora = strHour, estado = estado, nombre = nombre, totales = totales.toFloat(),
                                    sede = sedeTareo, oprod = oprod)
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

        dniT.setText("")
        advanceT.setText("")
        timeE.text = ""

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