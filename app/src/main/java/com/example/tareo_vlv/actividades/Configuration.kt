package com.example.tareo_vlv.actividades

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.tareo_vlv.R
import com.example.tareo_vlv.database.comedorCRUD
import com.example.tareo_vlv.model.CCenterModel
import com.example.tareo_vlv.model.ComedorModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class Configuration : Fragment() {

    private lateinit var descanso: TextInputLayout
    private lateinit var descansoT: TextInputEditText
    private lateinit var saveDescanso: Button

    private lateinit var configCRUD: comedorCRUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_configuration, container, false)
        val context: Context = requireContext()

        configCRUD = comedorCRUD(context)

        initView(view)
        saveDescanzo()
        return view
    }

    private fun initView(view: View){

        descanso = view.findViewById(R.id.descanso)
        descansoT = view.findViewById(R.id.descansoT)
        saveDescanso = view.findViewById(R.id.saveDescanso)

    }

    private fun saveDescanzo(){


        saveDescanso.setOnClickListener {
            val descansoT = descansoT.text.toString()
            val dMinutos = "0."+descansoT
            configCRUD.deleteComedor()

            println(dMinutos)

            configCRUD.insertComedor(
                ComedorModel(
                    id = 1
                    ,dMinutos
                )
            )
            Toast.makeText(context, "Minutos comedor registrado correctamente", Toast.LENGTH_SHORT).show()
        }
    }

}