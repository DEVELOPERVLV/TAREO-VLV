package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.ComedorModel
import com.example.tareo_vlv.model.LaborModel

class comedorCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertComedor(com: ComedorModel):Long{

        val db: SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()

        contentValues.put(TareoContract.Companion.ComedorContract.IDCOMEDOR, com.id)
        contentValues.put(TareoContract.Companion.ComedorContract.MINUTOS, com.minutos)

        val success = db.insert(TareoContract.Companion.ComedorContract.TBL_COMEDOR, null, contentValues)

        db.close()

        return success

    }

    fun selectComedor(): ArrayList<ComedorModel> {

        val item: ArrayList<ComedorModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(

            TareoContract.Companion.ComedorContract.IDCOMEDOR,
            TareoContract.Companion.ComedorContract.MINUTOS,

        )

        val c: Cursor = db.query(
            TareoContract.Companion.ComedorContract.TBL_COMEDOR,
            columnas,
            null,
            null,
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item.add(ComedorModel(
                c.getInt(c.getColumnIndexOrThrow(TareoContract.Companion.ComedorContract.IDCOMEDOR)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ComedorContract.MINUTOS)),
                )
            )
        }

        c.close()

        return item

    }

    fun deleteComedor(){
        val db:SQLiteDatabase = helper?.writableDatabase!!
        db.delete(TareoContract.Companion.ComedorContract.TBL_COMEDOR, null, null)
        db.close()
    }

}