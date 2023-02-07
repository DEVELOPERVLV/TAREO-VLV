package com.example.tareo_vlv.model

class SedeModel(
    idSede: String,
    sede: String,
    empresa: String,) {

    var idSede: String? = null
    var sede: String? = null
    var empresa: String? = null
    init {
        this.idSede = idSede
        this.sede = sede
        this.empresa = empresa
    }

}