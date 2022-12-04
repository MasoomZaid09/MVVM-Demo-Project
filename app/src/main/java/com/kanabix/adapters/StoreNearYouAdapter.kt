package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.StoreListDoc
import com.kanabix.interfaces.StoreClick
import com.kanabix.interfaces.StoreWishListClick
import com.kanabix.interfaces.WishListClick

class StoreNearYouAdapter ( var context :Context, var data : ArrayList<StoreListDoc>, var click : StoreClick,var wishListClick : StoreWishListClick) : RecyclerView.Adapter<StoreNearYouAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.storeitem_layout, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = data[position]

        holder.CategoryCV.setOnClickListener {
            click.sendStoreId(data.id)
        }

        Glide.with(context).load(data.store.storeImage).placeholder(R.drawable.store_placeholder).into(holder.image)
        holder.storeName.text = data.store.storeName
        holder.merchantName.text = "Merchant : ${data.store.merchantName}"
        holder.txtNoOfRating.text = data.noOfLikes.toString()
        holder.txtNoOfProduct.text = data.noOfProducts.toString()


        if (data.isliked){
            holder.LikeHeart.visibility = View.VISIBLE
            holder.unLikeHeart.visibility = View.GONE
        }else{
            holder.LikeHeart.visibility = View.GONE
            holder.unLikeHeart.visibility = View.VISIBLE
        }

        holder.unLikeHeart.setOnClickListener {

            if (!data.isliked){
                wishListClick.storeWishListClick(data.id)
//                holder.LikeHeart.visibility = View.VISIBLE
//                holder.unLikeHeart.visibility = View.GONE
            }
        }

        holder.LikeHeart.setOnClickListener {

            if (data.isliked) {
                wishListClick.storeWishListClick(data.id)
//                holder.unLikeHeart.visibility = View.GONE
//                holder.LikeHeart.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var CategoryCV: LinearLayout
        var LikeHeart: ImageView
        var unLikeHeart: ImageView
        var image: ImageView
        var storeName: TextView
        var merchantName: TextView
        var txtNoOfRating: TextView
        var txtNoOfProduct: TextView

        init {
            CategoryCV = itemView.findViewById(R.id.CategoryCV)
            LikeHeart = itemView.findViewById(R.id.LikeHeart)
            unLikeHeart = itemView.findViewById(R.id.unLikeHeart)
            image = itemView.findViewById(R.id.image)
            storeName = itemView.findViewById(R.id.storeName)
            merchantName = itemView.findViewById(R.id.merchantName)
            txtNoOfRating = itemView.findViewById(R.id.txtNoOfRating)
            txtNoOfProduct = itemView.findViewById(R.id.txtNoOfProduct)
        }
    }
}