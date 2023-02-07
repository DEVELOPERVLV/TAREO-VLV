package com.example.tareo_vlv.model

class CultivoModel(
    idCultivo: String,
    dni: String,
    descripcion: String,
) {
    var idCultivo: String? = null
    var dni: String? = null
    var descripcion: String? = null

    init {
        this.idCultivo = idCultivo
        this.dni = dni
        this.descripcion = descripcion
    }
}