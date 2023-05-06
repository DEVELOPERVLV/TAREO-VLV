package com.example.tareo_vlv.model

class OProdModel(idoprod: String,
                 idconsumidor: String,
                 descripcion: String,) {

    var idoprod: String? = null
    var idconsumidor: String? = null
    var descripcion: String? = null

    init {
        this.idoprod = idoprod
        this.idconsumidor = idconsumidor
        this.descripcion = descripcion
    }
}