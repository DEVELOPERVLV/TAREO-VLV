package com.example.tareo_vlv.model

class OProdModel(idoprod: String,
                 idconsumidor: String,
                 idmanual: String,) {

    var idoprod: String? = null
    var idconsumidor: String? = null
    var idmanual: String? = null

    init {
        this.idoprod = idoprod
        this.idconsumidor = idconsumidor
        this.idmanual = idmanual
    }
}