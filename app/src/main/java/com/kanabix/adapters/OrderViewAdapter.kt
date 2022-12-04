package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.ViewOrderProductDetail
import com.kanabix.utils.SavedPrefManager

class OrderViewAdapter(
    var context: Context,
    var data: ArrayList<ViewOrderProductDetail>,
): RecyclerView.Adapter<OrderViewAdapter.ViewHolder>() {
    val role = SavedPrefManager.getStringPreferences(context, SavedPrefManager.ROLE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.order_description_model_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val data = data[position]

            holder.imageView_arrow.visibility = View.GONE

            Glide.with(context).load(data.productId.thumbnail).placeholder(R.drawable.placholder).into(holder.product_img)
            holder.tv_product_name.text = "Merchant : ${data.productId.userId.name}"
            holder.brand.text = data.productId.productName
            holder.tv_qty.text = "Qty:${data.quantityGet.toString()}"
            holder.txtPrice.text = "Item Price : ${CommonFunctions.currencyFormatter(data.productId.price.toDouble())}/10 g"
//
//            if (data.deliveryDateAndTime != null){
//                holder.date.text = "${DateFormat.ordersDate(data.productId.deliveryDateAndTime)}"
//            }else {
                holder.date.text = "${DateFormat.ordersDate(data.productId.updatedAt)}"
//            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {


        val layoutMain = itemView.findViewById<ConstraintLayout>(R.id.layoutMain)
        val tv_product_name = itemView.findViewById<TextView>(R.id.tv_product_name)
        val brand = itemView.findViewById<TextView>(R.id.brand)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        val tv_qty = itemView.findViewById<TextView>(R.id.tv_qty)
        val date = itemView.findViewById<TextView>(R.id.date)
        val product_img = itemView.findViewById<ImageView>(R.id.product_img)
        val imageView_arrow = itemView.findViewById<ImageView>(R.id.imageView_arrow)

    }
}
