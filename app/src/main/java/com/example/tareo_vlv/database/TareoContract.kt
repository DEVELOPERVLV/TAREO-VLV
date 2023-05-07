package com.example.tareo_vlv.contract

import android.provider.BaseColumns

class TareoContract {

    companion object{
        const val DATABASE_VERSION = 1
        class Entrada: BaseColumns{
            companion object{
                const val TBL_PRE = "preTareo"
                const val ID = "idPre"
                const val USER = "user"
                const val COSTCENTER = "costcenter"
                const val ACTIVITY = "activity"
                const val JOB = "job"
                const val ADVANCE = "advance"
                const val TIMEI = "timeI"
                const val TIMEF = "timeF"
                const val TIMEE = "timeE"
                const val DNI = "dni"
                const val DIA = "dia"
                const val HORA = "hora"
                const val STATUS = "estado"
                const val NOMBRES = "nombres"
                const val TOTALES = "totales"
                const val SEDE = "sede"
                const val OPROD = "oprod"
            }
        }

        class CultivoContract: BaseColumns{
            companion object{
                const val TBL_CULTIVO = "cultivo"
                const val IDCULTIVO = "idCultivo"
                const val DNI = "DNI"
                const val DESCRIPCION = "DESCRIPCION"

            }
        }
        class CcenterContract: BaseColumns{
            companion object{
                const val TBL_CENTER = "centroCostos"
                const val IDCULTIVO = "IDCULTIVO"
                const val IDCONSUMIDOR = "IDCONSUMIDOR"

            }
        }

        class OProdContract: BaseColumns{
            companion object{
                const val TBL_OPROD = "ordenProduccion"
                const val IDOPROD = "IDOPROD"
                const val IDCONSUMIDOR = "IDCONSUMIDOR"
                const val IDMANUAL = "idmanual"
            }
        }

        class ActivityContract: BaseColumns{
            companion object{
                const val TBL_ACTIVITY = "actividad"
                const val IDACTIVIDAD = "IDACTIVIDAD"
                const val DESCRIPCION = "DESCRIPCION"
                const val RENDIMIENTO = "POR_RENDIMIENTO"
                const val IDCONSUMIDOR = "IDCONSUMIDOR"
            }
        }
        class LaboresContract: BaseColumns{
            companion object{
                const val TBL_LAB = "labores"
                const val CODIGO = "codigo"
                const val IDLABOR = "idlabor"
                const val DESCRIPCION = "descripcion"
                const val CANTIDAD = "cantidad"
            }
        }
        class PersonalContract: BaseColumns{
            companion object{
                const val TBL_PERSONAL = "personal"
                const val IDCODIGOGENERAL = "IDCODIGOGENERAL"
                const val TRABAJADOR = "TRABAJADOR"
            }
        }
        class TareadorContract: BaseColumns{
            companion object{
                const val TBL_TAREADOR = "tareador"
                const val DNI = "DNI"
                const val SEDE = "SEDE"

            }
        }

            class SedeContract: BaseColumns{
                companion object{
                    const val TBL_SEDES = "sucursal"
                    const val IDSEDE = "IDSUCURSAL"
                    const val SEDE = "DESCRIPCION"
                    const val EMPRESA = "IDEMPRESA"
                }
            }
    }
}