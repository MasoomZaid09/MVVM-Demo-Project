package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.PaymentListResult
import com.kanabix.interfaces.PaymentManagement
import com.kanabix.interfaces.PaymentManagementListener
import com.kanabix.models.OrderPendingModelClass
import com.kanabix.models.PaymentReciptModelClass

class PaymentReciptAdapter(
    var context: Context,
    var data: ArrayList<PaymentListResult>,
    var click: PaymentManagementListener

    ): RecyclerView.Adapter<PaymentReciptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.payment_recipt_modellayout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]


//        Glide.with(context).load(Data.image).into(holder.image)
//        holder.orderId.text = Data.orderId
//        holder.deliveryDate.text = Data.deliveryDate
//        holder.transactionId.text = Data.transactionId
//        holder.amount.text = Data.amount

        holder.paymentCardView.setOnClickListener{
            click.paymentListener("")
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {


        var paymentCardView = view.findViewById<CardView>(R.id.paymentCardView)
        var image = view.findViewById<ImageView>(R.id.image)
        var orderId = view.findViewById<TextView>(R.id.orderId)
        var deliveryDate = view.findViewById<TextView>(R.id.deliveryDate)
        var transactionId = view.findViewById<TextView>(R.id.transactionId)
        var amount = view.findViewById<TextView>(R.id.amount)


    }}