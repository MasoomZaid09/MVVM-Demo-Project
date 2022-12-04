package com.kanabix.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.kanabix.R
import com.kanabix.api.response.CartListResponse
import com.kanabix.customclicks.CommonInterface
import com.kanabix.customclicks.adaptorCartItemListener
import com.kanabix.customclicks.deleteClick
import com.kanabix.models.CartModelClass
import com.kanabix.models.CartSummeryModelClass
import com.kanabix.models.DeleviryAddNewAddressModelClass
import kotlin.math.round

class CartSummeryAdapter(
    var context: Context,
    var data: List<CartListResponse.CartListResult>

    ): RecyclerView.Adapter<CartSummeryAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.cart_summery_modellayout, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartListData = data[position]


        Glide.with(context).load(cartListData.productId.thumbnail).placeholder(R.drawable.placholder).into(holder.product_img)
        holder.tv_product_name.text = "${cartListData.productId.productName} (${cartListData.quantity})"
        holder.tv_discription.text = cartListData.productId.categoryId.categoryName
        holder.tv_newPrice.text =
           "${CommonFunctions.currencyFormatter(cartListData.totalPrice.toDouble())}/10 g"

        if (cartListData.addType.equals("PRODUCT")) {
            holder.tv_priceOff.visibility = View.GONE
            holder.tv_oldPrice.text = ""
            holder.tv_priceOff.text = ""

        } else {
            holder.tv_priceOff.visibility = View.VISIBLE
            holder.tv_oldPrice.text = "${CommonFunctions.currencyFormatter(cartListData.productId.price.toDouble())}/10 g"
            var perfirst = cartListData.productId.price.toDouble() - cartListData.totalPrice.toDouble()
            var per = perfirst * 100
            var finalPer = per / cartListData.productId.price.toDouble()
            holder.tv_priceOff.text = "${finalPer.toInt()}% off"
        }

        holder.tv_oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var product_img: ImageView =view.findViewById(R.id.product_img)
        var delete: ImageView =view.findViewById(R.id.img_cancle)
        var tv_product_name: TextView =view.findViewById(R.id.tv_product_name)
        var tv_discription: TextView =view.findViewById(R.id.tv_discription)
        var tv_newPrice: TextView =view.findViewById(R.id.tv_newPrice)
        var tv_oldPrice: TextView =view.findViewById(R.id.tv_oldPrice)
        var tv_priceOff: TextView =view.findViewById(R.id.tv_priceOff)
        var tv_add: TextView =view.findViewById(R.id.tv_add)
        var tv_deleviry_add: TextView =view.findViewById(R.id.tv_deleviry_add)

    }}