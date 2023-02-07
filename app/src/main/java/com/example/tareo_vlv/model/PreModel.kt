package com.example.tareo_vlv.model

class PreModel(

    id: Int?,
    user: String,
    costcenter: String,
    activity: String,
    job: String,
    advance: String,
    timeI: String,
    timeF: String,
    timeE: String,
    dni: String,
    dia: String,
    hora: String,
    estado: Int,
    nombre: String,
    totales: Float,
    sede: String,

) {

    var id:Int? = null
    var user: String? = null
    var costcenter:String? = null
    var activity:String? = null
    var job: String? = null
    var advance:String? = null
    var timeI: String? = null
    var timeF: String? = null
    var timeE: String? = null
    var dni:String? = null
    var dia: String? = null
    var hora: String? = null
    var estado: Int? = null
    var nombre: String? = null
    var totales: Float? = null
    var sede: String? = null

    init {

        this.id = id
        this.user = user
        this.costcenter = costcenter
        this.activity = activity
        this.job = job
        this.advance = advance
        this.timeI = timeI
        this.timeF = timeF
        this.timeE = timeE
        this.dni = dni
        this.dia = dia
        this.hora = hora
        this.estado = estado
        this.nombre = nombre
        this.totales = totales
        this.sede = sede

    }

}