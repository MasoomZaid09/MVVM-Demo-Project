package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R

class ImageSliderAdapter (var context : Context, var data:ArrayList<String>
        ) : RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {

            val view : View? =  LayoutInflater.from(parent.context).inflate(R.layout.slider_image, null)
            view?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)!!
            return MyViewHolder(view!!)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


            Glide.with(context).load(data.get(position)).placeholder(R.drawable.placholder).into(holder.image)

        }

        override fun getItemCount(): Int {
            return data.size
        }


        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var image = itemView.findViewById<ImageView>(R.id.image)
        }

}