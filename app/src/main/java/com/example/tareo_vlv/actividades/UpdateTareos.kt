package com.example.tareo_vlv.actividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tareo_vlv.R
import com.example.tareo_vlv.crud.TareoCRUD
import com.example.tareo_vlv.model.PreModel
import com.example.tareo_vlv.recyclerView.AdaptadorCustom
import com.example.tareo_vlv.recyclerView.ClickListener
import com.example.tareo_vlv.recyclerView.LongClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton


class UpdateTareos : Fragment() {

    private lateinit var listTareo: RecyclerView
    private lateinit var adaptador: AdaptadorCustom
    private lateinit var layoutManager: RecyclerView.LayoutManager

    var listareo: ArrayList<PreModel>? = null
    private var temtareo: ArrayList<PreModel>? = null

    private var crud: TareoCRUD? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_update_tareos, container, false)

        val context: Context = requireContext()
        listTareo = view.findViewById(R.id.listTareo)
        listTareo.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        listTareo.layoutManager = layoutManager

        crud = TareoCRUD(context)

        listareo = crud?.getAllPre()
        temtareo?.addAll(listareo!!)

        adaptador = AdaptadorCustom(listareo!!, object: ClickListener {
            override fun onClick(view: View, index: Int) {
                val intent = Intent(context, Update::class.java)
                intent.putExtra("idPre", listareo!![index].id.toString())
                startActivity(intent)

            }

        },
            object : LongClickListener {

                override fun longClick(view: View, index: Int) {
                    val v = View.inflate(context, R.layout.dialog_view, null)
                    val builder = AlertDialog.Builder(context)
                    val nombreAlerta = v.findViewById<TextView>(R.id.nameA)
                    val jobAlerta = v.findViewById<TextView>(R.id.jobA)
                    nombreAlerta.text = listareo!![index].nombre.toString()
                    jobAlerta.text = listareo!![index].job.toString()
                    val btnEliminar = v.findViewById<Button>(R.id.btnELimina)
                    builder.setView(v)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    btnEliminar.setOnClickListener {
                        crud?.dropAllTareo(listareo!![index].id.toString().toInt())
                        dialog.dismiss()
                        val intent = Intent(context, UpdateTareo::class.java)
                        startActivity(intent)
                    }
                }
            })

        listTareo.adapter = adaptador

        return view

    }


}