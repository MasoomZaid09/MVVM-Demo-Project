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
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.DeliveryOrderListDoc
import com.kanabix.interfaces.OrderManagementClick
import com.kanabix.models.OrderPendingModelClass
import com.kanabix.utils.SavedPrefManager

class OrderPendingAdapter(
    var context: Context,
    var data: ArrayList<DeliveryOrderListDoc>,
    var click: OrderManagementClick
): RecyclerView.Adapter<OrderPendingAdapter.ViewHolder>() {
    val role = SavedPrefManager.getStringPreferences(context, SavedPrefManager.ROLE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.orderpending_modellayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]



        Glide.with(context).load(Data.productDetails.get(0).productId.thumbnail).placeholder(R.drawable.placholder).into(holder.image)
        holder.customername.text = Data.userId.name
        holder.orderid.text = Data.orderId
        holder.orderdate.text = DateFormat.dealsdate(Data.createdAt)
        holder.productStatus.text = Data.deliveryPartnerStatus

        if (role.equals("Customer")){
            holder.imgArrow.visibility = View.VISIBLE
        }else{
            holder.imgArrow.visibility = View.GONE
        }

       holder.cardview_Product.setOnClickListener{

           click.orderManagementClick(Data.id)

       }


    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {


        var cardview_Product = view.findViewById<CardView>(R.id.Pending_cardview)
        var image = view.findViewById<ImageView>(R.id.image)
        var customername = view.findViewById<TextView>(R.id.customername)
        var orderid = view.findViewById<TextView>(R.id.orderid)
        var orderdate = view.findViewById<TextView>(R.id.orderdate)
        var productStatus = view.findViewById<TextView>(R.id.productStatus)
        var imgArrow = view.findViewById<ImageView>(R.id.imgArrow)


    }}
