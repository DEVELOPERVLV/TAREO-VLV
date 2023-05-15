package com.example.tareo_vlv.actividades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.tareo_vlv.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class Configuration : Fragment() {

    private lateinit var descanso: TextInputLayout
    private lateinit var descansoT: TextInputEditText
    private lateinit var saveDescanso: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_configuration, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View){

        descanso = view.findViewById(R.id.descanso)
        descansoT = view.findViewById(R.id.descansoT)
        saveDescanso = view.findViewById(R.id.saveDescanso)

    }

}