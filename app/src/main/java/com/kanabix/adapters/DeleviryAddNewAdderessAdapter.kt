package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.AddressListResponse
import com.kanabix.customclicks.*
import com.kanabix.models.CartModelClass
import com.kanabix.models.DeleviryAddNewAddressModelClass

class DeleviryAddNewAdderessAdapter(
    var context: Context,
    var data: List<AddressListResponse.AddressListResult.AddressListDoc>,
    var click: editAddressListener,
    var viewClick: getAddressIdListener,
    var delete : ChooseDeliveryAddressDelete
    ): RecyclerView.Adapter<DeleviryAddNewAdderessAdapter.ViewHolder>() {

    var flag = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.deleviry_add_new_address_modellayout, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressListData = data[position]

        holder.tv_name.text = addressListData.name
        holder.tv_address.text = "${addressListData.address}, ${addressListData.city}, ${addressListData.state}, ${addressListData.country}"
        holder.tv_pincode.text = addressListData.zipCode
        holder.tv_number.text = "${addressListData.countryCode}-${addressListData.mobileNumber}"

        holder.img_edit.setOnClickListener {
            click.editAddress(addressListData.id)
        }

        holder.ll_address.setOnClickListener{
            viewClick.getAddressIdClick(addressListData.id)
        }

        holder.deleteClick.setOnClickListener {
            delete.deleteClick(addressListData.id,position)
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {


        var tv_name: TextView =view.findViewById(R.id.tv_name)
        var tv_address: TextView =view.findViewById(R.id.tv_address)
        var tv_pincode: TextView =view.findViewById(R.id.tv_pincode)
        var tv_number: TextView =view.findViewById(R.id.tv_number)
        var img_edit: ImageView =view.findViewById(R.id.img_edit)
        var deleteClick: ImageView =view.findViewById(R.id.deleteClick)
        var ll_address: LinearLayout =view.findViewById(R.id.ll_address)

    }}