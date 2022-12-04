package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kanabix.R
import com.kanabix.api.response.CountryStateCityListResult
import com.kanabix.interfaces.popupItemClickListner
import com.kanabix.models.Popup


class OpenPopUp(
    var context: Context,
    var data: ArrayList<CountryStateCityListResult>,
    var flag: String,
    var click: popupItemClickListner
) :
    RecyclerView.Adapter<OpenPopUp.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.lists, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]


        if (flag == "Country"){
            holder.Names.text = Data.name
            holder.Names.setOnClickListener {
                Data.name?.let { it1 -> click.getData(it1,flag,Data.isoCode) }
            }

        }else if (flag == "State"){
            holder.Names.text = Data.name
            holder.Names.setOnClickListener {
                Data.name?.let { it1 -> click.getData(it1,flag,Data.isoCode) }
            }

        }else if (flag == "City"){
            holder.Names.text = Data.name
            holder.Names.setOnClickListener {
                Data.name?.let { it1 -> click.getData(it1,flag,Data.isoCode) }
            }

        }else if (flag == "Filter"){
            holder.Names.text = Data.name
            holder.Names.setOnClickListener {
                Data.name?.let { it1 -> click.getData(it1,flag,"") }
            }

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun filterData(filterData: ArrayList<CountryStateCityListResult>) {
        data = filterData
        notifyDataSetChanged()
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var Names: TextView



        init {
            Names = itemView.findViewById(R.id.content_textView)

        }
    }




}