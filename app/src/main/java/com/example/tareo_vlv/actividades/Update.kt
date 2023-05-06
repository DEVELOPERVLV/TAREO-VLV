package com.example.tareo_vlv.actividades

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import com.example.tareo_vlv.database.ActiviyCRUD
import com.example.tareo_vlv.database.LaborCRUD
import com.example.tareo_vlv.database.TrabajadorCRUD
import com.example.tareo_vlv.model.PreModel
import com.example.tareo_vlv.model.TrabajadorModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class Update : AppCompatActivity(){

    private lateinit var dni: TextInputEditText
    private lateinit var ccostos: TextInputEditText
    private lateinit var phase: Spinner
    private lateinit var jobU: Spinner
    private lateinit var advance: EditText
    private lateinit var horasI: TextInputEditText
    private lateinit var horasF: TextInputEditText
    private lateinit var timeE : Button
    private lateinit var hourE : TextInputEditText
    private lateinit var fButton: FloatingActionButton
    private lateinit var btnActualizar: Button
    private lateinit var rendimiento: TextView
    private lateinit var cantidad: TextView
    private lateinit var tcrud: TrabajadorCRUD
    private lateinit var acrud: ActiviyCRUD
    private var crud: TareoCRUD? = null
    private var lcrud: LaborCRUD? = null

    var c = 0
    private var performanceAct = ""
    var activity = ""
    var activityU = ""
    var rendimientoAct = ""
    private var foodDiscount = true
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //ESTE CODIGO OMITE EL MODO OSCURO
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //ESTE CODIGO MANTIENE LA PANTALLA ENCENDIDA MIENTRAS ESTE ABIERTA LA APLICACION
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_update)

        val index = intent.getStringExtra("idPre")

        crud = TareoCRUD(this)
        lcrud = LaborCRUD(this)
        tcrud = TrabajadorCRUD(this)
        acrud = ActiviyCRUD(this)

        initView()

        getTareos(index.toString())

        val listareo =crud?.getTareo(index)
        val id = listareo?.id.toString().toInt()
        val user = listareo?.user.toString()
        val ccenter = listareo?.costcenter.toString()
        activity = listareo?.activity.toString()
        updateActividad(index.toString(), ccenter.trim())
        val estado: Int = listareo?.estado.toString().toInt()
        val dia = listareo?.dia.toString()
        val hora = listareo?.hora.toString()
        listareo?.totales.toString()
        val rendimiento = acrud.selectActivityT(listareo?.activity.toString(), ccenter.trim())
        val food = listareo?.timeE.toString()
        val sede = listareo?.sede.toString()

        addFood(food)

        performanceAct += rendimiento.rendimiento.toString()

        val estadoI: Int = estado + 1

        if (estado < 10){

            btnActualizar.setOnClickListener{

                updateTareo(id, user, ccenter, dia, hora, estadoI, sede)

            }

        }else{

            btnActualizar.isEnabled = false

        }

        fButton.setOnClickListener{

            startActivity(Intent(this,UpdateTareo::class.java))
            finish()

        }

    }

    private fun initView(){

        ccostos = findViewById(R.id.ccostos)
        dni = findViewById(R.id.dni)
        advance = findViewById(R.id.advance)
        btnActualizar = findViewById(R.id.btnActualizar)
        fButton = findViewById(R.id.fButton)
        horasI = findViewById(R.id.horasI)
        horasF = findViewById(R.id.horasF)
        timeE = findViewById(R.id.timeE)
        hourE = findViewById(R.id.hourE)
        phase = findViewById(R.id.phase)
        jobU = findViewById(R.id.jobU)
        rendimiento = findViewById(R.id.rendimiento)
        cantidad = findViewById(R.id.cantidad)

        ccostos.isEnabled = false
    }

    private fun updateTareo(
        id: Int, user: String, costcenter: String, dia: String,
        hora: String, estado: Int, sede: String
    ) {

        val dni = dni.text.toString()
        val avance = advance.text.toString()
        val horaI = horasI.text.toString()
        val horaF = horasF.text.toString()
        val activity = phase.selectedItem.toString()
        val job = jobU.selectedItem.toString()
        val hourE = hourE.text.toString()
        val hIni = calculationHour(horaI)
        val hFin = calculationHour(horaF)
        val totalLabor = validaHora(hIni, hFin, hourE.toDouble())

        println("Horas:::" + hIni + " " + hFin)
        crud?.updateTareo(PreModel(

            id = id,
            user = user,
            costcenter = costcenter,
            activity = activity.substring(0,3),
            job = job,
            advance = avance,
            timeI = horaI,
            timeF = horaF,
            timeE = hourE,
            dni = dni,
            dia = dia,
            hora = hora,
            estado = estado,
            nombre = actualizarDNI(dni),
            totales = totalLabor.toFloat(),
            sede = sede,
            oprod = ""
            ))

        startActivity(Intent(this, UpdateTareo::class.java))
        finish()

    }

    private fun getTareos(index: String){

        val listareo =crud?.getTareo(index)
        ccostos.setText(listareo!!.costcenter.toString())
        dni.setText(listareo.dni, TextView.BufferType.EDITABLE)
        advance.setText(listareo.advance.toString(), TextView.BufferType.EDITABLE)
        horasI.setText(listareo.timeI, TextView.BufferType.EDITABLE)
        horasF.setText(listareo.timeF, TextView.BufferType.EDITABLE)

    }

    private fun updateActividad(index: String, ccostos: String) {

        val listareo = crud?.getTareo(index)
        val activitys = acrud.selectActividad(ccostos)
        val listaActividad = arrayListOf<String>()

        for (i in 0 until activitys.size) {

            if (listareo != null) {

                if (activitys[i].idactividad.toString() != listareo.activity.toString()) {

                    c += 1

                } else {

                    break

                }
            }
        }

        for (i in 0 until activitys.size) {

            listaActividad.add(activitys[i].idactividad.toString() + " - " + activitys[i].descripcion.toString())

        }

        val adapterAct = ArrayAdapter(this, R.layout.spinner_list, listaActividad)
        phase.adapter = adapterAct
        phase.setSelection(c)
        phase.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                activityU = activitys[p2].idactividad.toString()
                rendimientoAct = activitys[p2].rendimiento.toString()

                if(activityU == activity){
                    updateLabor(rendimientoAct, index, activityU)
                }else{
                    listaLabor(rendimientoAct, index, activityU)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {



            }
        }

    }

    private fun updateLabor(rendimientoAct: String, index: String, actividad:String) {

        var cont = 0
        val listareo = crud?.getTareo(index)
        val labores = lcrud?.selectLabor(actividad)

        if (labores != null) {

            val listaLabor = arrayListOf<String>()

            for (i in 0 until labores.size) {

                if (listareo != null) {

                    if (labores[i].idlabor.toString() != listareo.job.toString().substring(0,6)) {

                        cont += 1

                    } else {

                        break

                    }
                }
            }


            for (i in 0 until labores.size) {

                listaLabor.add(labores[i].idlabor.toString() + " - " + labores[i].descripcion.toString())

                println("Labores::"+labores[i].descripcion.toString())
            }

            val adapterLab = ArrayAdapter(this, R.layout.spinner_list, listaLabor)

            jobU.adapter = adapterLab
            jobU.setSelection(cont)

            jobU.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val cantidades = labores[p2].cantidad.toString()

                    if(rendimientoAct == "NO"){

                        rendimiento.text = getString(R.string.TJob)

                    }else{

                        rendimiento.text = getString(R.string.EJob)

                    }

                    cantidad.text = cantidades
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }


            }
        }
    }

    private fun listaLabor(rendimientoAct: String, index: String, actividad:String){

        crud?.getTareo(index)

        val labores = lcrud?.selectLabor(actividad)

        if (labores != null) {

            val listaLabor = arrayListOf<String>()

            for (i in 0 until labores.size) {

                listaLabor.add(labores[i].idlabor.toString() + " - " + labores[i].descripcion.toString())

            }

            val adapterLab = ArrayAdapter(this, R.layout.spinner_list, listaLabor)
            jobU.adapter = adapterLab
            jobU.setSelection(0)

            jobU.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    val cantidades = labores[p2].cantidad.toString()

                    if (rendimientoAct == "NO") {

                        rendimiento.text = getString(R.string.TJob)

                    } else {

                        rendimiento.text = getString(R.string.EJob)

                    }

                    cantidad.text = cantidades

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                    TODO("Not yet implemented")

                }

            }
        }
    }

    private fun actualizarDNI(dniu: String): String{
        var newName = ""

        val resultado = getTrabajador(dniu)

        if(resultado){

            val personalR: TrabajadorModel? = tcrud.selectPersonal(dniu)

            val nombren = personalR?.trabajador.toString()

            newName += if(nombren == "null"){

                "Usuario no registrado o valide DNI"

            }else{

                personalR?.trabajador.toString()

            }
        }

        return newName

    }

    private fun getTrabajador(dni: String):Boolean{

        val personalR: TrabajadorModel? = tcrud.selectPersonal(dni)
        return personalR?.equals(null) != true

    }

    private fun validaHora(horaI: Double, horaF: Double, dscto: Double): Double {
        val dsctos: Double

        if (dscto == 0.00){
            dsctos = 0.00
        }else{
            dsctos = dscto / 0.60
        }

        return (horaF - horaI) - (dsctos)

    }

    private fun addFood(food: String){

        foodDiscount = if (food == "0.54"){
            hourE.setText(R.string.foodAc)

            timeE.setBackgroundColor(Color.parseColor("#912A84"))

            false

        }else{
            hourE.setText(R.string.foodN)

            timeE.setBackgroundColor(Color.parseColor("#FFFFFFFF"))

            true
        }

        timeE.setOnClickListener {
            if (foodDiscount) {

                hourE.setText(R.string.foodAc)

                timeE.setBackgroundColor(Color.parseColor("#912A84"))

                foodDiscount = false

            } else {

                hourE.setText(R.string.foodN)

                foodDiscount = true

                timeE.setBackgroundColor(Color.parseColor("#FFFFFFFF"))

            }
        }
    }

    private fun calculationHour(hourI: String): Double {

        val minute: List<String> = hourI.split('.')
        val hour = minute[0].toDouble()
        val newMinute = minute[1].toDouble() / 60
        println("nuevoMinuto::: "+newMinute)
        return hour + newMinute
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == 4){
            val intent = Intent(applicationContext, UpdateTareo::class.java)
            startActivity(intent)
        }
        return super.onKeyDown(keyCode, event)
    }
}