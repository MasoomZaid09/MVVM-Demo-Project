package com.kanabix.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.kanabix.R
import com.kanabix.api.response.CartListResponse
import com.kanabix.customclicks.adaptorCartItemListener
import com.kanabix.customclicks.cartUpdateListener
import kotlin.math.round

class CartAdapter(
    var context: Context,
    var data: List<CartListResponse.CartListResult>,
    var click: adaptorCartItemListener,
    var cartUpdateClick: cartUpdateListener

) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var quantity = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mInflater = LayoutInflater.from(context)
        val view = mInflater.inflate(R.layout.cart_modellayout, parent, false)




        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carListData = data[position]

        if (carListData.productId.thumbnail == "") {
            Glide.with(context).load("").placeholder(R.drawable.placholder).into(holder.product_img)
        } else {
            Glide.with(context).load(carListData.productId.thumbnail).placeholder(R.drawable.cart_product_placeholder).into(holder.product_img)
        }

        holder.tv_product_name.text = carListData.productId.productName
        holder.tv_discription.text = carListData.productId.categoryId.categoryName
        holder.tv_newPrice.text = "${CommonFunctions.currencyFormatter(carListData.totalPrice.toDouble())}"
        if (carListData.addType.equals("PRODUCT")) {
            holder.tv_priceOff.visibility = View.GONE
            holder.tv_oldPrice.text = ""
            holder.tv_priceOff.text = ""
        } else {
            holder.tv_priceOff.visibility = View.VISIBLE

            holder.tv_oldPrice.text =
                "${CommonFunctions.currencyFormatter(carListData.productId.price.toDouble())}"
            var perfirst =
                carListData.productId.price.toDouble() - carListData.totalPrice.toDouble()
            var per = perfirst * 100
            var finalPer = per / carListData.productId.price.toDouble()
            holder.tv_priceOff.text = "${finalPer.toInt()}% off"
        }

        holder.tv_quantity.text = carListData.quantity.toString()


        holder.decreament.setOnClickListener {
            var count = Integer.parseInt(holder.tv_quantity.text.toString())
            if (holder.tv_quantity.text.toString().equals("1")) {
                quantity = holder.tv_quantity.text.toString()
                try {
                    val builder = AlertDialog.Builder(context)
                    builder.setCancelable(true)
                    builder.setIcon(R.drawable.ic_green_cart)
                    builder.setTitle("Delete")
                    builder.setMessage("Are you sure you want to Delete?")
                    builder.setPositiveButton("Yes") { dialog, which ->
                        click.adaptorItemDeleteClick(
                            0,
                            carListData.totalPrice.toDouble(),
                            carListData.id,
                            quantity,
                            holder.tv_quantity
                        )
                    }
                    builder.setNegativeButton("No") { dialog, which -> }

                    val dialog = builder.create()
                    dialog.show()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                count--
//                holder.tv_quantity.text = count.toString()
                quantity = holder.tv_quantity.text.toString()
                cartUpdateClick.decrementAmount(
                    count,
                    carListData.totalPrice.toDouble(),
                    carListData.id,
                    quantity,
                    holder.tv_quantity,
                    holder.IncAndDecLL,
                    holder.loader
                )
            }
        }

        holder.increament.setOnClickListener {
            var count = Integer.parseInt(holder.tv_quantity.text.toString())
            count++
//            holder.tv_quantity.text = count.toString()
            quantity = holder.tv_quantity.text.toString()

            cartUpdateClick.incrementAmount(
                count,
                carListData.totalPrice.toDouble(),
                carListData.id,
                quantity,
                holder.tv_quantity,
                holder.IncAndDecLL,
                holder.loader
            )
        }

        holder.tv_oldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        holder.delete.setOnClickListener {
            quantity = holder.tv_quantity.text.toString()
            click.adaptorItemDeleteClick(
                0,
                carListData.totalPrice.toDouble(),
                carListData.id,
                quantity,
                holder.tv_quantity
            )
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var product_img: ImageView = view.findViewById(R.id.product_img)
        var delete: ImageView = view.findViewById(R.id.img_cancle)
        var tv_product_name: TextView = view.findViewById(R.id.tv_product_name)
        var tv_discription: TextView = view.findViewById(R.id.tv_discription)
        var tv_newPrice: TextView = view.findViewById(R.id.tv_newPrice)
        var tv_oldPrice: TextView = view.findViewById(R.id.tv_oldPrice)
        var tv_priceOff: TextView = view.findViewById(R.id.tv_priceOff)
        var tv_quantity: TextView = view.findViewById(R.id.tv_quantity)
        var decreament: RelativeLayout = view.findViewById(R.id.decrement)
        var increament: RelativeLayout = view.findViewById(R.id.increment)
        var IncAndDecLL: LinearLayout = view.findViewById(R.id.IncAndDecLL)
        var loader: LottieAnimationView = view.findViewById(R.id.loader)

    }
}