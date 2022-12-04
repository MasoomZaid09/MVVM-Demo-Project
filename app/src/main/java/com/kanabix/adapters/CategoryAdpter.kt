package com.exobe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.customclicks.CommonInterface
import com.kanabix.interfaces.CategoryClick

class CategoryAdpter(val context: Context, val itemList: ArrayList<CategoryManagementDoc>, var click : CategoryClick) :
    RecyclerView.Adapter<CategoryAdpter.ProductLatestViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductLatestViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.latest_product_model_layout, parent, false)
        return ProductLatestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductLatestViewHolder, position: Int) {
        val orderData = itemList[position]

        holder.txtItem.text = orderData.categoryName
        Glide.with(context).load(orderData.categoryImage).placeholder(R.drawable.placholder).into(holder.productListImage1)

        holder.touch_LL.setOnClickListener {
            click.categoryClick(orderData.id)
        }

    }

    fun addAll(itemList: ArrayList<CategoryManagementDoc>) {
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ProductLatestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productListImage1: ImageView
        var touch_LL: LinearLayout
        var txtItem: TextView

        init {
            productListImage1 = itemView.findViewById(R.id.productListImage)
            txtItem = itemView.findViewById(R.id.productListText)
            touch_LL = itemView.findViewById(R.id.touch_LinearLayout)
        }

    }

}