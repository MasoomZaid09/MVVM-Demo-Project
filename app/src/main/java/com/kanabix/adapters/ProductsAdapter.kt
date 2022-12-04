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
import com.kanabix.api.response.ProductListDoc
import com.kanabix.interfaces.ProductClick
import com.kanabix.interfaces.WishListClick
import com.kanabix.interfaces.wishListClickUpdated

class ProductsAdapter(
    var context: Context, var data: ArrayList<ProductListDoc>, var productClick: ProductClick,var wishListClick : wishListClickUpdated, var flag :String) :RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val view: View = mInflater.inflate(R.layout.product_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        try {

            Glide.with(context).load(Data.thumbnail).placeholder(R.drawable.placholder).into(holder.ImageCategory)
            holder.name.setText(Data.productName)
            holder.price.setText("${CommonFunctions.currencyFormatter(Data.price.toDouble())}/10 g")

            holder.CategoryCV.setOnClickListener {
                productClick.sendProductId(Data.id)
            }

            if (Data.isliked){
                holder.LikeHeart.visibility = View.VISIBLE
                holder.unLikeHeart.visibility = View.GONE
            }else{
                holder.LikeHeart.visibility = View.GONE
                holder.unLikeHeart.visibility = View.VISIBLE
            }

            holder.unLikeHeart.setOnClickListener {

                wishListClick.wishListClick(Data.id,holder.unLikeHeart, holder.LikeHeart)
//                holder.LikeHeart.visibility = View.VISIBLE
//                holder.unLikeHeart.visibility = View.GONE
            }

            holder.LikeHeart.setOnClickListener {

                wishListClick.wishListClick(Data.id,holder.unLikeHeart, holder.LikeHeart)
//                holder.unLikeHeart.visibility = View.VISIBLE
//                holder.LikeHeart.visibility = View.GONE
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        if (flag.equals("Home")){
            if (data.size >= 4){
                return 4
            }else{
                return data.size
            }
        }else{
            return data.size
        }
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
            unLikeHeart = itemView.findViewById(R.id.unLikeHeart)
            LikeHeart = itemView.findViewById(R.id.LikeHeart)
        }
    }
}
