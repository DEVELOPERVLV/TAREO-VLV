package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.TareadorModel

import kotlin.collections.ArrayList

class TareadorCRUD(context: Context) {
    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertTareador(tar: TareadorModel):Long{

        val db: SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()

        contentValues.put(TareoContract.Companion.TareadorContract.DNI, tar.dni)
        contentValues.put(TareoContract.Companion.TareadorContract.SEDE, tar.sede)


        val success = db.insert(TareoContract.Companion.TareadorContract.TBL_TAREADOR, null, contentValues)

        db.close()

        return success
    }

    fun getTareador(DNI: String?, sede: String?):TareadorModel?{

        var item: TareadorModel? = null

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(

            TareoContract.Companion.TareadorContract.DNI,
            TareoContract.Companion.TareadorContract.SEDE,

            )

        val c: Cursor = db.query(

            TareoContract.Companion.TareadorContract.TBL_TAREADOR,
            columnas,
            "DNI = ? and SEDE = ?",
            arrayOf(DNI, sede),
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item =
                TareadorModel(
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.TareadorContract.DNI)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.TareadorContract.SEDE)))


        }

        c.close()

        return item

    }

    fun selectCCostosDNI(dni: String): ArrayList<TareadorModel> {
        val item: ArrayList<TareadorModel> = ArrayList()
        val db:SQLiteDatabase = helper?.readableDatabase!!
        val columnas = arrayOf(
            TareoContract.Companion.TareadorContract.DNI,
            TareoContract.Companion.TareadorContract.SEDE,
        )
        val c: Cursor = db.query(
            TareoContract.Companion.TareadorContract.TBL_TAREADOR,
            columnas,
            "DNI = ?",
            arrayOf(dni),
            null,
            null,
            null
        )
        while (c.moveToNext()){
            item.add(
                TareadorModel(
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.TareadorContract.DNI)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.TareadorContract.SEDE))
                )
            )
        }

        c.close()
        return item
    }

    fun deleteTareador(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.TareadorContract.TBL_TAREADOR)
        db.close()
    }
}