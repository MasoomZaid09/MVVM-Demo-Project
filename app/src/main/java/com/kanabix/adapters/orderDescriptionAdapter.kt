package com.kanabix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.produceState
import androidx.recyclerview.widget.RecyclerView
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.ViewOrderTrackingArray
import com.kanabix.models.orderDescriptionModelClass

class orderDescriptionAdapter(
    val context: Context,
    val itemList: ArrayList<orderDescriptionModelClass>,
) :
    RecyclerView.Adapter<orderDescriptionAdapter.LastOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastOrderViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.order_description_modellayout_second, parent, false)
        return LastOrderViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onBindViewHolder(holder: LastOrderViewHolder, position: Int) {


        itemList[position].apply {
//
//            if (statusName.equals("ORDER PLACED")) {
//
//                holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
//                holder.iView.setBackgroundColor(Color.parseColor("#416654"))
//                holder.itemName.text = itemName
//                holder.itemDesc.text = DateFormat.dealsdate(createdAt)
//            }
//            if (statusName.equals("ORDER PLACED")) {
//                holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
//                holder.iView.setBackgroundColor(Color.parseColor("#416654"))
//                holder.itemName.text = itemName
//                holder.itemDesc.text = itemDesc
//            }
//
//            if (statusName.equals("PICKED")) {
//                holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
//                holder.iView.setBackgroundColor(Color.parseColor("#416654"))
//                holder.itemName.text = itemName
//                holder.itemDesc.text = itemDesc
//            }
//
//            if (statusName.equals("SHIPPED")) {
//                holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
//                holder.iView.setBackgroundColor(Color.parseColor("#416654"))
//                holder.itemName.text = itemName
//                holder.itemDesc.text = itemDesc
//            }
//
//            if (statusName.equals("DELIVERED")) {
//                holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
//                holder.iView.setBackgroundColor(Color.parseColor("#416654"))
//                holder.itemName.text = itemName
//                holder.itemDesc.text = itemDesc
//
//            } else {
//                holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
//                holder.iView.setBackgroundColor(Color.GRAY)
//                holder.itemName.text = itemName
//                holder.itemDesc.text = itemDesc
//            }
//            if (position == 4) {
//                holder.iView.visibility = View.GONE
//            }

            if (position == 0){

                    if(statusFlag) {
                        if (statusName.equals("ORDER PLACED")) {
                            holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
                            holder.iView.setBackgroundColor(Color.parseColor("#416654"))
                            holder.itemName.text = itemName
                            holder.itemDesc.text = DateFormat.dealsdate(createdAt)
                        }
                    } else {
                        holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
                        holder.iView.setBackgroundColor(Color.GRAY)
                        holder.itemName.text = itemName
                        holder.itemDesc.text = DateFormat.dealsdate(createdAt)
                    }

            }else if(position == 1){
                    if(statusFlag) {
                        if (statusName.equals("ORDER PLACED")) {
                            holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
                            holder.iView.setBackgroundColor(Color.parseColor("#416654"))
                            holder.itemName.text = itemName
                            holder.itemDesc.text = itemDesc
                        }
                    }else {
                        holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
                        holder.iView.setBackgroundColor(Color.GRAY)
                        holder.itemName.text = itemName
                        holder.itemDesc.text = itemDesc
                    }

            } else if(position == 2){
                if(statusFlag) {
                    if (statusName.equals("PICKED"))
                        {
                            holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
                            holder.iView.setBackgroundColor(Color.parseColor("#416654"))
                            holder.itemName.text = itemName
                            holder.itemDesc.text = itemDesc
                        }
                }else {
                    holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
                    holder.iView.setBackgroundColor(Color.GRAY)
                    holder.itemName.text = itemName
                    holder.itemDesc.text = itemDesc
                }
            } else if(position == 3){
                if(statusFlag) {
                    if (statusName.equals("SHIPPED")) {
                        holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
                        holder.iView.setBackgroundColor(Color.parseColor("#416654"))
                        holder.itemName.text = itemName
                        holder.itemDesc.text = itemDesc
                    }
                }else {
                    holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
                    holder.iView.setBackgroundColor(Color.GRAY)
                    holder.itemName.text = itemName
                    holder.itemDesc.text = itemDesc
                }
            }  else if(position == 4){
                if(statusFlag) {
                    if (statusName.equals("DELIVERED")) {
                        holder.itemImage.setBackgroundResource(R.drawable.ic_baseline_circle_24)
                        holder.iView.setBackgroundColor(Color.parseColor("#416654"))
                        holder.itemName.text = itemName
                        holder.itemDesc.text = itemDesc
                    }
                }else {
                    holder.itemImage.setBackgroundResource(R.drawable.ic_outline_circle_24)
                    holder.iView.setBackgroundColor(Color.GRAY)
                    holder.itemName.text = itemName
                    holder.itemDesc.text = itemDesc
                }
            }

            if (position == 4) {
                holder.iView.visibility = View.GONE
            }
        }

    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class LastOrderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val itemImage = view.findViewById<ImageView>(R.id.img)
        val itemName = view.findViewById<TextView>(R.id.item_header)
        val itemDesc = view.findViewById<TextView>(R.id.item_desc)
        val iView = view.findViewById<View>(R.id.view)
    }
}