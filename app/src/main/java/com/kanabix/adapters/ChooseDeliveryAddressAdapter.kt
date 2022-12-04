package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kanabix.R
import com.kanabix.api.response.AddressListResponse
import com.kanabix.customclicks.ChooseDeliveryAddressDelete
import com.kanabix.customclicks.editAddressListener
import com.kanabix.interfaces.PaymentManagement

class ChooseDeliveryAddressAdapter(
    var context: Context,
    var itemList: List<AddressListResponse.AddressListResult.AddressListDoc>,
    var click: PaymentManagement,
    var editAddress: editAddressListener,
    var deleteClick: ChooseDeliveryAddressDelete
//    var flag: String

) : RecyclerView.Adapter<ChooseDeliveryAddressAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_delivery_address, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressData = itemList[position]

        holder.firstname.text = addressData.name
        holder.address.text = "${addressData.address}, ${addressData.city}, ${addressData.state}, ${addressData.country}"
        holder.pincode.text = "Pincode:- ${addressData.zipCode}"
        holder.phonenumber.text = "+${addressData.countryCode} ${ addressData.mobileNumber }"

        holder.edit.setOnClickListener {
            editAddress.editAddress(addressData.id)
        }

        holder.chooseDelivery.setOnClickListener {

            click.PaymentManagementClick(position)
        }

        holder.delete.setOnClickListener {
            deleteClick.deleteClick(addressData.id,position)
        }


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstname: TextView
        var address: TextView
        var pincode: TextView
        var phonenumber: TextView
        var name: TextView
        var edit: ImageView
        var chooseDelivery: LinearLayout
        var delete: ImageView


        init {
            firstname = itemView.findViewById(R.id.firstname)
            address = itemView.findViewById(R.id.address)
            pincode = itemView.findViewById(R.id.pincode)
            phonenumber = itemView.findViewById(R.id.phonenumber)
            edit = itemView.findViewById(R.id.editaddress)
            chooseDelivery = itemView.findViewById(R.id.chooseDelivery)
            delete = itemView.findViewById(R.id.delete)
            name = itemView.findViewById(R.id.firstname)


        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }


}