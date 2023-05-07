package com.example.tareo_vlv.actividades

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.tareo_vlv.R
import com.example.tareo_vlv.actividades.PreRegister
import com.example.tareo_vlv.actividades.tareoSOP


private lateinit var sinOP: CardView
private lateinit var conOP: CardView

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view:View = inflater.inflate(R.layout.fragment_home, container, false)

        initView(view)

        conOp()

        sinOp()


        return view




    }

    private fun initView(view: View){
        conOP = view.findViewById(R.id.conOP)
        sinOP = view.findViewById(R.id.sinOP)
    }

    private fun sinOp() {
        sinOP.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, TareoSinOp())?.commit()
        }
    }

    private fun conOp(){
        conOP.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, TareoConOP())?.commit()
        }
    }

}