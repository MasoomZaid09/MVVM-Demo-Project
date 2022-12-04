package com.kanabix.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.NotificationListDoc
import com.kanabix.customclicks.NotificationDeleteListener
import com.kanabix.customclicks.NotificationViewListener
import com.kanabix.customclicks.deleteClick

class NotificationAdapter(
    var notificationDeleteListener: NotificationDeleteListener,
    var notificationViewListener: NotificationViewListener,
    var notificationList: ArrayList<NotificationListDoc>
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        var view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_layout, null)
        return MyViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val notificationListData = notificationList.get(position)
            holder.notification_body.text = notificationListData.body
            holder.notification_date.text = DateFormat.NotificationDateFormat(notificationListData.createdAt)
            holder.notification_time.text = DateFormat.NotificationTimeFormat(notificationListData.createdAt)

            holder.delete.setOnClickListener {
                notificationDeleteListener.notificationOpenPopUp(notificationListData.id)
            }

            holder.notification_view_ll.setOnClickListener {
                notificationViewListener.notificationViewClick(notificationListData.notifyType,notificationListData.id)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var notification_body = itemView.findViewById<TextView>(R.id.notification_body)
        var notification_date = itemView.findViewById<TextView>(R.id.notification_date)
        var notification_time = itemView.findViewById<TextView>(R.id.notification_time)
        var notification_view_ll = itemView.findViewById<LinearLayout>(R.id.notification_view_ll)
        var delete = itemView.findViewById<LinearLayout>(R.id.delete)
    }
}