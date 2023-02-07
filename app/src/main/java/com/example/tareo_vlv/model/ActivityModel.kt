package com.example.tareo_vlv.model

class ActivityModel(
        idactividad: String,
        descripcion: String,
        rendimiento: String,
        idconsumidor: String,
) {

    var idactividad: String? = null
    var descripcion: String? = null
    var rendimiento: String? = null
    var idconsumidor: String? = null

    init {
        this.idactividad = idactividad
        this.descripcion = descripcion
        this.rendimiento = rendimiento
        this.idconsumidor = idconsumidor
    }

}