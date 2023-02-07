package com.example.tareo_vlv.model

class TrabajadorModel(
    idcodigogeneral: String,
    trabajador: String) {
    var idcodigogeneral: String? = null;
    var trabajador: String? = null;


    init {
        this.idcodigogeneral = idcodigogeneral;
        this.trabajador = trabajador;
    }
}