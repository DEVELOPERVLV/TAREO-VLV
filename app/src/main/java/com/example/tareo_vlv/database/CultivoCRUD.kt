package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.actividades.TareoConOP
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.CultivoModel

class CultivoCRUD(context: Context) {
    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertCultivo(cul: CultivoModel):Long{
        val db: SQLiteDatabase = helper?.writableDatabase!!
        val contentValues= ContentValues()

        contentValues.put(TareoContract.Companion.CultivoContract.IDCULTIVO, cul.idCultivo)
        contentValues.put(TareoContract.Companion.CultivoContract.DNI, cul.dni)
        contentValues.put(TareoContract.Companion.CultivoContract.DESCRIPCION, cul.descripcion)

        val success = db.insert(TareoContract.Companion.CultivoContract.TBL_CULTIVO, null, contentValues)

        //db.close()

        return success

    }

    fun selectCultivo(dni: String): ArrayList<CultivoModel>{

        val item: ArrayList<CultivoModel> = ArrayList()

        val db: SQLiteDatabase = helper?.readableDatabase!!

        val columnas= arrayOf(
            TareoContract.Companion.CultivoContract.IDCULTIVO,
            TareoContract.Companion.CultivoContract.DNI,
            TareoContract.Companion.CultivoContract.DESCRIPCION,
        )

        val c: Cursor = db.query(
            TareoContract.Companion.CultivoContract.TBL_CULTIVO,
            columnas,
            "DNI = ? ",
            arrayOf(dni),
            null,
            null,
            null
        )

        while (c.moveToNext()){
            item.add(
                CultivoModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.CultivoContract.IDCULTIVO)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.CultivoContract.DNI)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.CultivoContract.DESCRIPCION))
            )
            )
        }
        c.close()
        return item
    }

    fun delelteCultivo(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from " + TareoContract.Companion.CultivoContract.TBL_CULTIVO)
    }

}