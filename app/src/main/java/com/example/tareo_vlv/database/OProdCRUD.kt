package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.OProdModel

class OProdCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertOProd(op: OProdModel): Long{

        val db: SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()

        contentValues.put(TareoContract.Companion.OProdContract.IDOPROD, op.idoprod)
        contentValues.put(TareoContract.Companion.OProdContract.IDCONSUMIDOR, op.idconsumidor)
        contentValues.put(TareoContract.Companion.OProdContract.DESCRIPCION, op.descripcion)

        val success = db.insert(TareoContract.Companion.OProdContract.TBL_OPROD, null, contentValues)

        return success

    }

    fun selectOP(costCenter: String): ArrayList<OProdModel>{

        val item: ArrayList<OProdModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.OProdContract.IDOPROD,
            TareoContract.Companion.OProdContract.IDCONSUMIDOR,
            TareoContract.Companion.OProdContract.DESCRIPCION
        )

        val c: Cursor = db.query(
            TareoContract.Companion.OProdContract.TBL_OPROD,
            columnas,
            "costCenter = ? ",
            arrayOf(costCenter),
            null,
            null,
            null
        )

        while (c.moveToNext()){
            item.add(
                OProdModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.OProdContract.IDOPROD)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.OProdContract.IDCONSUMIDOR)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.OProdContract.DESCRIPCION))
            )
            )
        }
        c.close()

        return item

    }

    fun deleteOP(){

        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.OProdContract.TBL_OPROD)

    }

}