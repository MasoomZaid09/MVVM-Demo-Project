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
import com.kanabix.api.response.StoreLikeListDoc
import com.kanabix.api.response.WishListDoc
import com.kanabix.interfaces.StoreClick
import com.kanabix.interfaces.StoreWishListClick
import com.kanabix.interfaces.WishListClick
import com.kanabix.interfaces.WishListUpdateClick

class StoreWishListAdapter(var context: Context, var data: ArrayList<StoreLikeListDoc>, var storeClick: StoreClick, var storeLikedClick : StoreWishListClick
):
    RecyclerView.Adapter<StoreWishListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val view: View = mInflater.inflate(R.layout.storeitem_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]

        try{
            Glide.with(context).load(data.storeId.store.storeImage).placeholder(R.drawable.placholder).into(holder.storeImage)
            holder.storeName.text = data.storeId.store.storeName
            holder.merchantName.text = "Merchant : ${data.storeId.store.merchantName}"
            holder.txtNoOfProduct.text = data.no_of_products.toString()
            holder.txtNoOfRating.text = data.no_of_likes.toString()
            holder.LikeHeart.visibility = View.VISIBLE
            holder.unLikeHeart.visibility = View.GONE
        }catch (e:Exception){
            e.printStackTrace()
        }



        holder.cardview.setOnClickListener {
            storeClick.sendStoreId(data.storeId.id)
        }


        holder.LikeHeart.setOnClickListener {
            storeLikedClick.storeWishListClick(data.storeId.id)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storeImage: ImageView
        var storeName: TextView
        var txtNoOfRating: TextView
        var txtNoOfProduct: TextView
        var merchantName: TextView
        var cardview: CardView
        var unLikeHeart: ImageView
        var LikeHeart: ImageView

        init {
            storeImage = itemView.findViewById(R.id.image)
            txtNoOfRating = itemView.findViewById(R.id.txtNoOfRating)
            txtNoOfProduct = itemView.findViewById(R.id.txtNoOfProduct)
            storeName = itemView.findViewById(R.id.storeName)
            merchantName = itemView.findViewById(R.id.merchantName)
            cardview = itemView.findViewById(R.id.cardview)
            unLikeHeart= itemView.findViewById(R.id.unLikeHeart)
            LikeHeart = itemView.findViewById(R.id.LikeHeart)
        }
    }
}
