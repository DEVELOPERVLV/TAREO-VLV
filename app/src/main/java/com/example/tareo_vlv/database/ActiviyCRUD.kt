package com.example.tareo_vlv.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tareo_vlv.contract.TareoContract
import com.example.tareo_vlv.model.ActivityModel

class ActiviyCRUD(context: Context) {

    private var helper: SQLiteOpenHelper? = null

    init {

        helper = SQLiteOpenHelper(context)

    }

    fun insertActivity(act: ActivityModel):Long{
        val db:SQLiteDatabase = helper?.writableDatabase!!

        val contentValues = ContentValues()
        contentValues.put(TareoContract.Companion.ActivityContract.IDACTIVIDAD, act.idactividad)
        contentValues.put(TareoContract.Companion.ActivityContract.DESCRIPCION, act.descripcion)
        contentValues.put(TareoContract.Companion.ActivityContract.RENDIMIENTO, act.rendimiento)
        contentValues.put(TareoContract.Companion.ActivityContract.IDCONSUMIDOR, act.idconsumidor)


        val success = db.insert(TareoContract.Companion.ActivityContract.TBL_ACTIVITY, null, contentValues)
        //db.close()

        return success
    }

    fun selectActivity(idconsumidor: String?):ArrayList<ActivityModel>{

        val item: ArrayList<ActivityModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.ActivityContract.IDACTIVIDAD,
            TareoContract.Companion.ActivityContract.DESCRIPCION,
            TareoContract.Companion.ActivityContract.RENDIMIENTO,
            TareoContract.Companion.ActivityContract.IDCONSUMIDOR
        )

        val c:Cursor = db.query(
            TareoContract.Companion.ActivityContract.TBL_ACTIVITY,
            columnas,
            " IDCONSUMIDOR = ? ",
            arrayOf(idconsumidor),
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item.add(
                ActivityModel(
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDACTIVIDAD)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.DESCRIPCION)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.RENDIMIENTO)),
                    c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDCONSUMIDOR)))
            )
        }

        c.close()

        return item

    }

    fun selectActivityT(activity: String?, ccostos: String?): ActivityModel{

        var item: ActivityModel? = null

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.ActivityContract.IDACTIVIDAD,
            TareoContract.Companion.ActivityContract.DESCRIPCION,
            TareoContract.Companion.ActivityContract.RENDIMIENTO,
            TareoContract.Companion.ActivityContract.IDCONSUMIDOR
        )

        val c:Cursor = db.query(
            TareoContract.Companion.ActivityContract.TBL_ACTIVITY,
            columnas,
            "IDACTIVIDAD = ? and IDCONSUMIDOR = ?",
            arrayOf(activity, ccostos),
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item = ActivityModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDACTIVIDAD)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.DESCRIPCION)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.RENDIMIENTO)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDCONSUMIDOR))
            )

        }

        c.close()

        return item!!

    }

    fun selectActividad(ccostos: String?): ArrayList<ActivityModel>{

        val item: ArrayList<ActivityModel> = ArrayList()

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            TareoContract.Companion.ActivityContract.IDACTIVIDAD,
            TareoContract.Companion.ActivityContract.DESCRIPCION,
            TareoContract.Companion.ActivityContract.RENDIMIENTO,
            TareoContract.Companion.ActivityContract.IDCONSUMIDOR
        )

        val c:Cursor = db.query(
            TareoContract.Companion.ActivityContract.TBL_ACTIVITY,
            columnas,
            " IDCONSUMIDOR = ?",
            arrayOf(ccostos),
            null,
            null,
            null

        )

        while (c.moveToNext()){
            item.add(ActivityModel(
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDACTIVIDAD)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.DESCRIPCION)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.RENDIMIENTO)),
                c.getString(c.getColumnIndexOrThrow(TareoContract.Companion.ActivityContract.IDCONSUMIDOR))
            )
            )
        }

        c.close()

        return item

    }

    fun deleteActivity(){
        val db:SQLiteDatabase = helper?.readableDatabase!!
        return db.execSQL("delete from "+TareoContract.Companion.ActivityContract.TBL_ACTIVITY)
    }
}