package com.example.tareo_vlv.model

class LaborModel(
        idactividad: String,
        idlabor: String,
        descripcion: String,
        cantidad: String,
        ){

    var idactividad: String? = null
    var idlabor: String? = null
    var descripcion:String? = null
    var cantidad: String? = null

    init {
        this.idactividad = idactividad
        this.idlabor = idlabor
        this.descripcion = descripcion
        this.cantidad = cantidad
    }
}