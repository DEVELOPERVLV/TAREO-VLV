package com.example.tareo_vlv.recyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tareo_vlv.R
import com.example.tareo_vlv.model.PreModel


class AdaptadorCustom(var tareoList: MutableList<PreModel>, private var listener: ClickListener, private var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>(),
    Filterable {

    var items: ArrayList<PreModel>

    private var itemsSeleccionados: ArrayList<Int>? = null
    private var viewHolder: ViewHolder? = null

    init {
        items = tareoList as ArrayList<PreModel> /* = java.util.ArrayList<com.example.tareo_vlv.model.PreModel> */
        itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_update, parent, false)

        viewHolder = ViewHolder(view, listener, longClickListener)
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)
        holder.id?.text = item.id.toString()
        holder.nombre?.text = item.nombre.toString()
        holder.dni?.text = item.dni.toString()
        holder.job?.text = item.job.toString()
        holder.dia?.text = item.dia.toString()
        holder.advance?.text = item.advance.toString()
        holder.hora?.text = item.totales.toString()

        if(itemsSeleccionados?.contains(position)!!){
            holder.view.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.view.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class ViewHolder(var view: View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(view),
        View.OnClickListener, View.OnLongClickListener{

        var id: TextView? = null
        var nombre: TextView? = null
        var dni: TextView? = null
        var job: TextView? = null
        var dia: TextView? = null
        var advance: TextView? = null
        var hora: TextView? = null
        private var listener: ClickListener? = null
        private var longListener: LongClickListener? = null

        init{
            id = view.findViewById(R.id.idTareo)
            nombre = view.findViewById(R.id.nombre)
            dni = view.findViewById(R.id.dni)
            job = view.findViewById(R.id.job)
            advance = view.findViewById(R.id.avance)
            hora = view.findViewById(R.id.hora)
            dia = view.findViewById(R.id.dia)

            this.listener = listener
            this.longListener = longClickListener

            view.setOnClickListener(this)
            view.setOnLongClickListener(this)

        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }

    }

    override fun getFilter(): Filter {
        return object:  Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charSearch = p0.toString()
                if(charSearch.isEmpty()){
                    items = tareoList as ArrayList<PreModel> /* = java.util.ArrayList<com.example.tareo_vlv.model.PreModel> */

                }else{
                    val resultList = ArrayList<PreModel>()
                    for (row in tareoList){
                        if (row.nombre?.lowercase()!!.contains(p0.toString().lowercase())
                            || row.dni?.lowercase()!!.contains(p0.toString().lowercase())
                            || row.job?.lowercase()!!.contains(p0.toString().lowercase())){
                            resultList.add(row)
                            }
                    }
                    items = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items = p1?.values as ArrayList<PreModel> /* = java.util.ArrayList<com.example.tareo_vlv.model.PreModel> */
                notifyDataSetChanged()
            }

        }
    }


}
