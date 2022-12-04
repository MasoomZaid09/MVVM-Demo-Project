package com.kanabix.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.DealListDoc
import com.kanabix.interfaces.DealProductClick

class KanabixDealsAdapter(
    var context: Context,
    var data: ArrayList<DealListDoc>,
    var dealProductClick: DealProductClick,
    var s: String
) :
    RecyclerView.Adapter<KanabixDealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val view: View = mInflater.inflate(R.layout.deal_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        Glide.with(context).load(Data.thumbnail).placeholder(R.drawable.placholder).into(holder.ImageCategory)

        holder.touch_LinearLayout.setOnClickListener {
            dealProductClick.DealProductClick(Data.id)
        }
    }

    override fun getItemCount(): Int {
        if (s.equals("Home")){
            if (data.size >= 3){
                return 3
            }else{
                return data.size
            }
        }else{
            return data.size
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageCategory: ImageView
        var touch_LinearLayout: LinearLayout

        init {
            ImageCategory = itemView.findViewById(R.id.productListImage)
            touch_LinearLayout = itemView.findViewById(R.id.touch_LinearLayout)
        }
    }

}
