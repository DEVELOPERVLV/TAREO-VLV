package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.LaborModel

class LaborCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertLabor(lab: LaborModel):Long{

        val db:SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()

        contentValues.put(TareoContract.Companion.LaboresContract.CODIGO, lab.codigo)
        contentValues.put(TareoContract.Companion.LaboresContract.IDLABOR, lab.idlabor)
        contentValues.put(TareoContract.Companion.LaboresContract.DESCRIPCION, lab.descripcion)
        contentValues.put(TareoContract.Companion.LaboresContract.CANTIDAD, lab.cantidad)

        val success = db.insert(TareoContract.Companion.LaboresContract.TBL_LAB, null, contentValues)

        //db.close()

        return success

    }

    fun selectLabor(codigo: String?):ArrayList<LaborModel>{

        val item: ArrayList<LaborModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.LaboresContract.CODIGO,
                TareoContract.Companion.LaboresContract.IDLABOR,
                TareoContract.Companion.LaboresContract.DESCRIPCION,
                TareoContract.Companion.LaboresContract.CANTIDAD
        )

        val c:Cursor = db.query(
                TareoContract.Companion.LaboresContract.TBL_LAB,
                columnas,
                " codigo = ? ",
                arrayOf(codigo),
                null,
                null,
                null

        )

        while (c.moveToNext()){
            item.add(LaborModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.CODIGO)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.IDLABOR)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.DESCRIPCION)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.CANTIDAD)))
            )
        }

        c.close()

        return item

    }

    fun updLabor(codigo: String?):ArrayList<LaborModel>{

        val item: ArrayList<LaborModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.LaboresContract.CODIGO,
            TareoContract.Companion.LaboresContract.IDLABOR,
            TareoContract.Companion.LaboresContract.DESCRIPCION,
            TareoContract.Companion.LaboresContract.CANTIDAD
        )

        val c:Cursor = db.query(
            TareoContract.Companion.LaboresContract.TBL_LAB,
            columnas,
            " codigo = ? ",
            arrayOf(codigo),
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item.add(LaborModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.CODIGO)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.IDLABOR)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.DESCRIPCION)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.LaboresContract.CANTIDAD)))
            )
        }

        c.close()

        return item

    }

    fun deleteLabor(){

        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.LaboresContract.TBL_LAB)

    }
}