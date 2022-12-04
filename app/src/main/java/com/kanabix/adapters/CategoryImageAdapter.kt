package com.kanabix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kanabix.R
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.interfaces.CategoryClick
import com.mikhaellopez.circularimageview.CircularImageView

class CategoryImageAdapter(
    var context: Context,
    var data: ArrayList<CategoryManagementDoc>,
    var click: CategoryClick,
    var s: String
) :
    RecyclerView.Adapter<CategoryImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view: View = mInflater.inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        Glide.with(context).load(Data.categoryImage).placeholder(R.drawable.placholder).into(holder.ImageCategory)
        holder.Description.setText(Data.categoryName)

        holder.layout.setOnClickListener {
            click.categoryClick(Data.id)
        }
    }


    override fun getItemCount(): Int {
        if (s.equals("Home")){
            if (data.size >= 5){
                return 5
            }else{
                return data.size
            }
        }else{
            return data.size
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageCategory: CircularImageView
        var Description: TextView
        var layout: LinearLayout

        init {
            ImageCategory = itemView.findViewById(R.id.ImageCategory)
            Description = itemView.findViewById(R.id.Description)
            layout = itemView.findViewById(R.id.layout)
        }
    }

}