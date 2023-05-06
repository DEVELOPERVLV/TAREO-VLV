package com.example.tareo_vlv.model

class LaborModel(
        codigo: String,
        idlabor: String,
        descripcion: String,
        cantidad: String,
        ){

    var codigo: String? = null
    var idlabor: String? = null
    var descripcion:String? = null
    var cantidad: String? = null

    init {
        this.codigo = codigo
        this.idlabor = idlabor
        this.descripcion = descripcion
        this.cantidad = cantidad
    }
}