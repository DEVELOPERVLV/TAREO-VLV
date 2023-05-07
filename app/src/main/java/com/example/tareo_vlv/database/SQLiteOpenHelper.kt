package com.example.tareo_vlv.database

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract


class SQLiteOpenHelper(context: Context):
    SQLiteOpenHelper(context, TareoContract.Companion.Entrada.TBL_PRE, null, TareoContract.DATABASE_VERSION){

    companion object{

        const val createTblTareo = "CREATE TABLE " + TareoContract.Companion.Entrada.TBL_PRE +
                " (" + TareoContract.Companion.Entrada.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TareoContract.Companion.Entrada.USER + " TEXT, " +
                TareoContract.Companion.Entrada.COSTCENTER + " TEXT, " +
                TareoContract.Companion.Entrada.ACTIVITY + " TEXT, "+
                TareoContract.Companion.Entrada.JOB + " TEXT, " +
                TareoContract.Companion.Entrada.ADVANCE + " TEXT, "+
                TareoContract.Companion.Entrada.TIMEI + " TEXT, " +
                TareoContract.Companion.Entrada.TIMEF + " TEXT, " +
                TareoContract.Companion.Entrada.TIMEE + " TEXT, " +
                TareoContract.Companion.Entrada.DNI + " TEXT, " +
                TareoContract.Companion.Entrada.DIA + " TEXT, "+
                TareoContract.Companion.Entrada.HORA + " TEXT, "+
                TareoContract.Companion.Entrada.STATUS + " TEXT, "+
                TareoContract.Companion.Entrada.NOMBRES + " TEXT, "+
                TareoContract.Companion.Entrada.TOTALES + " FLOAT, "+
                TareoContract.Companion.Entrada.SEDE + " TEXT, "+
                TareoContract.Companion.Entrada.OPROD + " TEXT )"

        const val rmvTblCompanyTareo = "DROP TABLE IF EXISTS " + TareoContract.Companion.Entrada.TBL_PRE

        const val createTblCultivo = "CREATE TABLE " + TareoContract.Companion.CultivoContract.TBL_CULTIVO +
                " (" + TareoContract.Companion.CultivoContract.IDCULTIVO + " TEXT, " +
                TareoContract.Companion.CultivoContract.DNI + " TEXT, " +
                TareoContract.Companion.CultivoContract.DESCRIPCION + " TEXT )"

        const val rmvTblCompanyCultivo = "DROP TABLE IF EXISTS " + TareoContract.Companion.CultivoContract.TBL_CULTIVO

        const val createTblCenter = "CREATE TABLE " + TareoContract.Companion.CcenterContract.TBL_CENTER +
                " (" + TareoContract.Companion.CcenterContract.IDCULTIVO + " TEXT, " +
                TareoContract.Companion.CcenterContract.IDCONSUMIDOR + " TEXT )"

        const val rmvTblCompanyCenter = "DROP TABLE IF EXISTS " + TareoContract.Companion.CcenterContract.TBL_CENTER


        const val createTblOprod = "CREATE TABLE " + TareoContract.Companion.OProdContract.TBL_OPROD +
                " (" + TareoContract.Companion.OProdContract.IDOPROD + " TEXT, " +
                TareoContract.Companion.OProdContract.IDCONSUMIDOR + " TEXT, " +
                TareoContract.Companion.OProdContract.IDMANUAL + " TEXT )"

        const val rmvTblCompanyOprod = "DROP TABLE IF EXISTS " + TareoContract.Companion.OProdContract.TBL_OPROD



        const val createTblActivity = "CREATE TABLE " + TareoContract.Companion.ActivityContract.TBL_ACTIVITY +
                " (" + TareoContract.Companion.ActivityContract.IDACTIVIDAD + " TEXT, " +
                TareoContract.Companion.ActivityContract.DESCRIPCION + " TEXT, " +
                TareoContract.Companion.ActivityContract.RENDIMIENTO + " TEXT, " +
                TareoContract.Companion.ActivityContract.IDCONSUMIDOR + " TEXT )"

        const val rmvTblCompanyActivity = "DROP TABLE IF EXISTS " + TareoContract.Companion.ActivityContract.TBL_ACTIVITY

        const val createTblLabores = "CREATE TABLE " + TareoContract.Companion.LaboresContract.TBL_LAB +
                " (" + TareoContract.Companion.LaboresContract.CODIGO + " TEXT, " +
                TareoContract.Companion.LaboresContract.IDLABOR + " TEXT, " +
                TareoContract.Companion.LaboresContract.DESCRIPCION + " TEXT, " +
                TareoContract.Companion.LaboresContract.CANTIDAD + " TEXT )"

        const val rmvTblCompanyLabores = "DROP TABLE IF EXISTS " + TareoContract.Companion.LaboresContract.TBL_LAB

        const val createTblPersonal = "CREATE TABLE " + TareoContract.Companion.PersonalContract.TBL_PERSONAL +
                " (" + TareoContract.Companion.PersonalContract.IDCODIGOGENERAL + " TEXT, " +
                TareoContract.Companion.PersonalContract.TRABAJADOR + " TEXT )"

        const val rmvTblCompanyPersonal = "DROP TABLE IF EXISTS " + TareoContract.Companion.PersonalContract.TBL_PERSONAL

        const val createTblTareador = "CREATE TABLE " + TareoContract.Companion.TareadorContract.TBL_TAREADOR +
                " (" + TareoContract.Companion.TareadorContract.DNI + " TEXT, " +
                TareoContract.Companion.TareadorContract.SEDE + " TEXT )"

        const val rmvTblCompanyTareador = "DROP TABLE IF EXISTS " + TareoContract.Companion.TareadorContract.TBL_TAREADOR

        const val createTblSede = "CREATE TABLE " + TareoContract.Companion.SedeContract.TBL_SEDES +
                " (" + TareoContract.Companion.SedeContract.IDSEDE + " TEXT, " +
                TareoContract.Companion.SedeContract.SEDE + " TEXT, " +
                TareoContract.Companion.SedeContract.EMPRESA + " TEXT )"

        const val rmvTblCompanySede = "DROP TABLE IF EXISTS " + TareoContract.Companion.SedeContract.TBL_SEDES
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(createTblPersonal)
        db?.execSQL(createTblTareo)
        db?.execSQL(createTblCultivo)
        db?.execSQL(createTblCenter)
        db?.execSQL(createTblOprod)
        db?.execSQL(createTblActivity)
        db?.execSQL(createTblLabores)
        db?.execSQL(createTblTareador)
        db?.execSQL(createTblSede)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL(rmvTblCompanyPersonal)
        db.execSQL(rmvTblCompanyTareo)
        db.execSQL(rmvTblCompanyCultivo)
        db.execSQL(rmvTblCompanyCenter)
        db.execSQL(rmvTblCompanyOprod)
        db.execSQL(rmvTblCompanyActivity)
        db.execSQL(rmvTblCompanyLabores)
        db.execSQL(rmvTblCompanyTareador)
        db.execSQL(rmvTblCompanySede)
        onCreate(db)

    }

    }