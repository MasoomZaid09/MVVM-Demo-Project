package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.BannerResult
import com.kanabix.api.response.DealListDoc

class BannerImageSliderView(
    var imageList: ArrayList<BannerResult>,
    var context: Context,


    ) : RecyclerView.Adapter<BannerImageSliderView.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        var view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.deals_card_view, null)
        view?.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )!!
        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = imageList[position]

        Glide.with(context).load(data.bannerImage).placeholder(R.drawable.placholder).into(holder.imageBanner)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageBanner = itemView.findViewById<ImageView>(R.id.imageBanner)

    }

}