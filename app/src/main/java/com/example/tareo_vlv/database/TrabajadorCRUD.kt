package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.TrabajadorModel

class TrabajadorCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {
        helper = SQLiteOpenHelper(context)
    }

    fun insertPersonal(tra: TrabajadorModel):Long{
        val db: SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()
        contentValues.put(TareoContract.Companion.PersonalContract.IDCODIGOGENERAL, tra.idcodigogeneral)
        contentValues.put(TareoContract.Companion.PersonalContract.TRABAJADOR, tra.trabajador)

        val success = db.insert(TareoContract.Companion.PersonalContract.TBL_PERSONAL, null, contentValues)
        db.close()

        return success

    }

    fun selectPersonal(idcodigogeneral: String): TrabajadorModel? {
        var item: TrabajadorModel? = null
        val db:SQLiteDatabase = helper?.readableDatabase!!
        val columnas = arrayOf(
            TareoContract.Companion.PersonalContract.IDCODIGOGENERAL,
            TareoContract.Companion.PersonalContract.TRABAJADOR
        )
        val c: Cursor = db.query(
            TareoContract.Companion.PersonalContract.TBL_PERSONAL,
            columnas,
            "IDCODIGOGENERAL = ?",
            arrayOf(idcodigogeneral),
            null,
            null,
            null
        )
            while (c.moveToNext()){
                item =
                    TrabajadorModel(
                        c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.PersonalContract.IDCODIGOGENERAL)),
                        c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.PersonalContract.TRABAJADOR))
                    )
            }
            c.close()
            return item
    }

    fun deleteTrabajador(){
        val db:SQLiteDatabase = helper?.writableDatabase!!
        db.delete(TareoContract.Companion.PersonalContract.TBL_PERSONAL, null, null)
        db.close()
    }
}