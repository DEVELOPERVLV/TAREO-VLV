package com.example.tareo_vlv.crud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.database.SQLiteOpenHelper
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.PreModel

class TareoCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {

        helper = SQLiteOpenHelper(context)

    }

    fun insertPre(pre: PreModel):Long{

        //Abri la base de datos en modo escritura
        val db: SQLiteDatabase = helper?.writableDatabase!!

        // Mapeo columnas a Insertar
        val contentValues = ContentValues()
        contentValues.put(TareoContract.Companion.Entrada.ID, pre.id)
        contentValues.put(TareoContract.Companion.Entrada.USER, pre.user)
        contentValues.put(TareoContract.Companion.Entrada.COSTCENTER, pre.costcenter)
        contentValues.put(TareoContract.Companion.Entrada.ACTIVITY, pre.activity)
        contentValues.put(TareoContract.Companion.Entrada.JOB, pre.job)
        contentValues.put(TareoContract.Companion.Entrada.ADVANCE, pre.advance)
        contentValues.put(TareoContract.Companion.Entrada.TIMEI, pre.timeI)
        contentValues.put(TareoContract.Companion.Entrada.TIMEF, pre.timeF)
        contentValues.put(TareoContract.Companion.Entrada.TIMEE, pre.timeE)
        contentValues.put(TareoContract.Companion.Entrada.DNI, pre.dni)
        contentValues.put(TareoContract.Companion.Entrada.DIA, pre.dia)
        contentValues.put(TareoContract.Companion.Entrada.HORA, pre.hora)
        contentValues.put(TareoContract.Companion.Entrada.STATUS, pre.estado)
        contentValues.put(TareoContract.Companion.Entrada.NOMBRES, pre.nombre)
        contentValues.put(TareoContract.Companion.Entrada.TOTALES, pre.totales)
        contentValues.put(TareoContract.Companion.Entrada.SEDE, pre.sede)

        val success = db.insert(TareoContract.Companion.Entrada.TBL_PRE, null, contentValues)
        db.close()

        return success

    }

    fun getAllPre(): ArrayList<PreModel> {

        val pre: ArrayList<PreModel> = ArrayList()
        val db: SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(TareoContract.Companion.Entrada.ID,
            TareoContract.Companion.Entrada.USER,
            TareoContract.Companion.Entrada.COSTCENTER,
            TareoContract.Companion.Entrada.ACTIVITY,
            TareoContract.Companion.Entrada.JOB,
            TareoContract.Companion.Entrada.ADVANCE,
            TareoContract.Companion.Entrada.TIMEI,
            TareoContract.Companion.Entrada.TIMEF,
            TareoContract.Companion.Entrada.TIMEE,
            TareoContract.Companion.Entrada.DNI,
            TareoContract.Companion.Entrada.DIA,
            TareoContract.Companion.Entrada.HORA,
            TareoContract.Companion.Entrada.STATUS,
            TareoContract.Companion.Entrada.NOMBRES,
            TareoContract.Companion.Entrada.TOTALES,
            TareoContract.Companion.Entrada.SEDE)

        val c:Cursor = db.query(TareoContract.Companion.Entrada.TBL_PRE,
            columnas,
            "estado != 11",
            null,
            null,
            null,
            null)

        while(c.moveToNext()){
            pre.add(PreModel(
                c.getInt(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ID)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.USER)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.COSTCENTER)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ACTIVITY)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.JOB)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ADVANCE)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEI)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEF)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEE)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.DNI)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.DIA)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.HORA)),
                c.getInt(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.STATUS)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.NOMBRES)),
                c.getFloat(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TOTALES)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.SEDE))
            ))
        }
        db.close()
        c.close()
        return pre
    }

    fun getTareo(idPre: String?): PreModel {

        var item: PreModel? = null

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(TareoContract.Companion.Entrada.ID,
            TareoContract.Companion.Entrada.USER,
            TareoContract.Companion.Entrada.COSTCENTER,
            TareoContract.Companion.Entrada.ACTIVITY,
            TareoContract.Companion.Entrada.JOB,
            TareoContract.Companion.Entrada.ADVANCE,
            TareoContract.Companion.Entrada.TIMEI,
            TareoContract.Companion.Entrada.TIMEF,
            TareoContract.Companion.Entrada.TIMEE,
            TareoContract.Companion.Entrada.DNI,
            TareoContract.Companion.Entrada.DIA,
            TareoContract.Companion.Entrada.HORA,
            TareoContract.Companion.Entrada.STATUS,
            TareoContract.Companion.Entrada.NOMBRES,
            TareoContract.Companion.Entrada.TOTALES,
            TareoContract.Companion.Entrada.SEDE)

        val c:Cursor = db.query(

            TareoContract.Companion.Entrada.TBL_PRE,
            columnas,
            " idPre = ? and estado != 11",
            arrayOf(idPre),
            null,
            null,
            null

        )

        while(c.moveToNext()){

            item = PreModel(c.getInt(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ID)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.USER)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.COSTCENTER)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ACTIVITY)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.JOB)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.ADVANCE)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEI)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEF)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TIMEE)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.DNI)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.DIA)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.HORA)),
                c.getInt(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.STATUS)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.NOMBRES)),
                c.getFloat(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.TOTALES)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.Entrada.SEDE))

            )

        }

        c.close()

        return item!!

    }

    fun updateTareo(pre: PreModel){

        val db:SQLiteDatabase = helper?.writableDatabase!!

        val values = ContentValues()
        values.put(TareoContract.Companion.Entrada.ID, pre.id)
        values.put(TareoContract.Companion.Entrada.USER, pre.user)
        values.put(TareoContract.Companion.Entrada.COSTCENTER, pre.costcenter)
        values.put(TareoContract.Companion.Entrada.ACTIVITY, pre.activity)
        values.put(TareoContract.Companion.Entrada.JOB, pre.job)
        values.put(TareoContract.Companion.Entrada.ADVANCE, pre.advance)
        values.put(TareoContract.Companion.Entrada.TIMEI, pre.timeI)
        values.put(TareoContract.Companion.Entrada.TIMEF, pre.timeF)
        values.put(TareoContract.Companion.Entrada.TIMEE, pre.timeE)
        values.put(TareoContract.Companion.Entrada.DNI, pre.dni)
        values.put(TareoContract.Companion.Entrada.DIA, pre.dia)
        values.put(TareoContract.Companion.Entrada.HORA, pre.hora)
        values.put(TareoContract.Companion.Entrada.STATUS, pre.estado)
        values.put(TareoContract.Companion.Entrada.NOMBRES, pre.nombre)
        values.put(TareoContract.Companion.Entrada.TOTALES, pre.totales)
        values.put(TareoContract.Companion.Entrada.SEDE, pre.sede)

        db.update(
            TareoContract.Companion.Entrada.TBL_PRE,
            values,
            " idPre = ?",
            arrayOf(pre.id.toString())
        )

        db.close()
    }

    fun getAllTareo(): Int {
        var countT = 0
        val db: SQLiteDatabase = helper?.readableDatabase!!

        val sql: String = "SELECT COUNT(*) FROM "+TareoContract.Companion.Entrada.TBL_PRE+ " WHERE estado != 11"

        val c:Cursor = db.rawQuery(sql, null)

        if (c.count > 0){
            c.moveToFirst()
            countT = c.getInt(0)
        }

        c.close()

        return countT

    }

    fun dropAllTareo(identi: Int){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.Entrada.TBL_PRE+ " WHERE idPre == " + identi)


    }

    fun deleteTareo(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.Entrada.TBL_PRE)


    }
}