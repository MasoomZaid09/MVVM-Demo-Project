package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.OrderListDoc
import com.kanabix.interfaces.OrderManagementClick
import kotlin.collections.ArrayList

class MyOrderAdapter(
    var context: Context,
    var data: ArrayList<OrderListDoc>,
    var click: OrderManagementClick
) : RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_order_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val data = data[position]

            holder.layoutMain.setOnClickListener {
                click.orderManagementClick(data.id)
            }

            Glide.with(context).load(data.productDetails[0].productId.thumbnail).placeholder(R.drawable.placholder).into(holder.product_img)
            holder.orderId.text = "#${data.orderId}"

            holder.txt_itemCount.text = "${data.productDetails.size} Item"
            holder.OrderStatus.text = data.orderStatus
            holder.txtItemPrice.setText("Total Amount : ${CommonFunctions.currencyFormatter(data.orderPrice.toDouble())}")

            if (data.orderStatus.equals("PENDING")){
                holder.date.text = "${DateFormat.ordersDate(data.deliveryDateAndTime)}"
                holder.txtDeliveryDate.text = "Expected Delivery Date :"

            }else if (data.orderStatus.equals("COMPLETED")){
                holder.txtDeliveryDate.text = "Delivered Date :"
                holder.date.text = "${DateFormat.ordersDate(data.deliveryDateAndTime)}"
            }

//            if (data.productDetails.get(0).quantityRequested == 1){
//                holder.txt_itemCount.text = "${data.productDetails.get(0).quantityRequested.toString()} Item" // needs to correct
//            }else{
//                holder.txt_itemCount.text = "${data.productDetails.get(0).quantityRequested.toString()} Items" // needs to correct
//            }



//            if (data.deliveryDateAndTime != null){
//                holder.date.text = "${DateFormat.ordersDate(data.deliveryDateAndTime)}"
//            }else{
//                holder.date.text = "${DateFormat.ordersDate(data.createdAt)}"
//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val layoutMain = itemView.findViewById<ConstraintLayout>(R.id.layoutMain)
        val txt_itemCount = itemView.findViewById<TextView>(R.id.txt_itemCount)
        val OrderStatus = itemView.findViewById<TextView>(R.id.OrderStatus)
        val txtItemPrice = itemView.findViewById<TextView>(R.id.txtItemPrice)
        val date = itemView.findViewById<TextView>(R.id.date)
        val product_img = itemView.findViewById<ImageView>(R.id.product_img)
        val orderId = itemView.findViewById<TextView>(R.id.orderId)
        val txtDeliveryDate = itemView.findViewById<TextView>(R.id.txtDeliveryDate)
    }
}