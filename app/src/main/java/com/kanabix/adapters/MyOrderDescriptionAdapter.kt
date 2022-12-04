package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.ViewOrderProductDetail
import com.kanabix.api.response.ViewOrderProductId
import com.kanabix.interfaces.OrderManagementClick
import com.kanabix.interfaces.PaymentManagement
import com.kanabix.interfaces.ProductClick

class MyOrderDescriptionAdapter(
    val context: Context,
    var data: ArrayList<ViewOrderProductDetail>,
    var click: ProductClick
) : RecyclerView.Adapter<MyOrderDescriptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.order_description_model_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val data = data[position]

            Glide.with(context).load(data.productId.thumbnail).placeholder(R.drawable.placholder).into(holder.product_img)
            holder.tv_product_name.text = "Merchant : ${data.productId.userId.name}"
            holder.brand.text = data.productId.productName
            holder.tv_qty.text = "Qty : ${data.quantityGet.toString()}"
            holder.txtPrice.text = "Item Price :${CommonFunctions.currencyFormatter(data.productId.price.toDouble())}/10 g"

            holder.layoutMain.setOnClickListener {
                click.sendProductId(data.productId.id)
            }

        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val layoutMain = itemView.findViewById<ConstraintLayout>(R.id.layoutMain)
        val tv_product_name = itemView.findViewById<TextView>(R.id.tv_product_name)
        val brand = itemView.findViewById<TextView>(R.id.brand)
        val tv_qty = itemView.findViewById<TextView>(R.id.tv_qty)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val product_img = itemView.findViewById<ImageView>(R.id.product_img)
    }


}