package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.kanabix.R
import com.kanabix.api.response.WishListDoc
import com.kanabix.interfaces.ProductClick
import com.kanabix.interfaces.WishListClick
import com.kanabix.interfaces.WishListUpdateClick
import com.kanabix.models.Products

class WishListAdapters(
    var context: Context, var data: ArrayList<WishListDoc>, var productClick: ProductClick, var wishListClick : WishListClick,var wishListUpdateClick: WishListUpdateClick
):
RecyclerView.Adapter<WishListAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val view: View = mInflater.inflate(R.layout.product_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        Data.productId.let {
            Glide.with(context).load(it.thumbnail).placeholder(R.drawable.placholder).into(holder.ImageCategory)
            holder.name.setText(it.productName)
            holder.price.setText("${CommonFunctions.currencyFormatter(it.price.toDouble())}/10 g")

            holder.CategoryCV.setOnClickListener { click ->
                productClick.sendProductId(it.id)
            }
        }

        if (Data.isLike){
            holder.LikeHeart.visibility = View.VISIBLE
            holder.unLikeHeart.visibility = View.GONE
        }else{
            holder.unLikeHeart.visibility = View.VISIBLE
            holder.LikeHeart.visibility = View.GONE
        }


        holder.LikeHeart.setOnClickListener {

            wishListClick.wishListClick(Data.productId.id)
            if (Data.isLike){
                wishListUpdateClick.updateWishList()
            }
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageCategory: ImageView
        var name: TextView
        var price: TextView
        var CategoryCV: LinearLayout
        var unLikeHeart: ImageView
        var LikeHeart: ImageView

        init {
            ImageCategory = itemView.findViewById(R.id.item_image)
            name = itemView.findViewById(R.id.itemTitle1)
            price = itemView.findViewById(R.id.itemValue2)
            CategoryCV = itemView.findViewById(R.id.CategoryCV)
            unLikeHeart= itemView.findViewById(R.id.unLikeHeart)
            LikeHeart = itemView.findViewById(R.id.LikeHeart)
        }
    }
}