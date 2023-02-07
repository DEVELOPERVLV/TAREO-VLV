package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.SedeModel

class SedeCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {

        helper = SQLiteOpenHelper(context)

    }

    fun insertSede(sed: SedeModel): Long{

        val db:SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()

        contentValues.put(TareoContract.Companion.SedeContract.IDSEDE, sed.idSede)
        contentValues.put(TareoContract.Companion.SedeContract.SEDE, sed.sede)
        contentValues.put(TareoContract.Companion.SedeContract.EMPRESA, sed.empresa)

        val success = db.insert(TareoContract.Companion.SedeContract.TBL_SEDES, null, contentValues)

        db.close()

        return success
    }

    fun selectSede(empresa: String): ArrayList<SedeModel>{
        val item:ArrayList<SedeModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columas = arrayOf(
            TareoContract.Companion.SedeContract.IDSEDE,
            TareoContract.Companion.SedeContract.SEDE,
            TareoContract.Companion.SedeContract.EMPRESA
        )

        val c: Cursor = db.query(
            TareoContract.Companion.SedeContract.TBL_SEDES,
            columas,
            " IDEMPRESA = ?",
            arrayOf(empresa),
            null,
            null,
            null
        )

        while (c.moveToNext()){

            item.add(
                SedeModel(
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.SedeContract.IDSEDE)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.SedeContract.SEDE)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.SedeContract.EMPRESA)))
            )
        }
        c.close()

        return item
    }

    fun deleteSedes(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.SedeContract.TBL_SEDES)
    }

}