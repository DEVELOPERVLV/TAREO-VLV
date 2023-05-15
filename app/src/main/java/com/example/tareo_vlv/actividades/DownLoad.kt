package com.example.tareo_vlv.actividades

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tareo_vlv.R
import com.example.tareo_vlv.R.xml.provider_paths
import com.example.tareo_vlv.crud.TareoCRUD
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DownLoad : Fragment() {

    private lateinit var downloadXlsx: Button
    private lateinit var tcrud: TareoCRUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_down_load, container, false)
        val context: Context = requireContext()
        tcrud = TareoCRUD(context)

        initView(view)
        downloadXlsx()
        return view
    }

    private fun initView(view: View){
        downloadXlsx = view.findViewById(R.id.tareoXlsx)
    }

    private fun downloadXlsx(){

        downloadXlsx.setOnClickListener {

            val resultado = tcrud.getAllPre()
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Tareo")

            for(i in 0 until resultado.size){

                val row = sheet.createRow(i)
                row.createCell(0).setCellValue(resultado[i].id.toString())
                row.createCell(1).setCellValue(resultado[i].dni.toString())
                row.createCell(2).setCellValue(resultado[i].nombre.toString())
                row.createCell(3).setCellValue(resultado[i].costcenter.toString())
                row.createCell(4).setCellValue(resultado[i].oprod.toString())
                row.createCell(5).setCellValue(resultado[i].activity.toString())
                row.createCell(6).setCellValue(resultado[i].job.toString())
                row.createCell(7).setCellValue(resultado[i].timeI.toString())
                row.createCell(8).setCellValue(resultado[i].timeF.toString())
                row.createCell(9).setCellValue(resultado[i].timeE.toString())
                row.createCell(10).setCellValue(resultado[i].totales.toString())

            }

            try {
                val archivo = File(provider_paths.toString())
                val fileOut = FileOutputStream(archivo)
                workbook.write(fileOut)
                fileOut.close()
                workbook.close()
            }catch (e: IOException){
                e.printStackTrace()
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }



        }


    }

}