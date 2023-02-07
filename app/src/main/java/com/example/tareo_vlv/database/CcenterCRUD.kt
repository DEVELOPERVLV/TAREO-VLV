package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.CCenterModel

class CcenterCRUD(context: Context) {
    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insetCCenter(cen: CCenterModel):Long{

        val db: SQLiteDatabase = helper?.writableDatabase!!

        val contentValues= ContentValues()

        contentValues.put(TareoContract.Companion.CcenterContract.IDCULTIVO, cen.idcultivo)
        contentValues.put(TareoContract.Companion.CcenterContract.IDCONSUMIDOR, cen.idconsumidor)

        val success = db.insert(TareoContract.Companion.CcenterContract.TBL_CENTER, null, contentValues)

        //db.close()

        return success

    }

    fun selectCCenter(dni: String):ArrayList<CCenterModel>{

        val item:ArrayList<CCenterModel> = ArrayList()

        val db: SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
                TareoContract.Companion.CcenterContract.IDCULTIVO,
                TareoContract.Companion.CcenterContract.IDCONSUMIDOR,
            
        )

        val c:Cursor = db.query(
                TareoContract.Companion.CcenterContract.TBL_CENTER,
                columnas,
                "IDCULTIVO = ?",
                arrayOf(dni),
                null,
                null,
                null
        )

        while (c.moveToNext()){

            item.add(CCenterModel(
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.CcenterContract.IDCULTIVO)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.CcenterContract.IDCONSUMIDOR))
            )
            )
        }

        c.close()

        return item
    }

    fun deleteCcenter(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.CcenterContract.TBL_CENTER)
    }

}