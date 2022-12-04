package com.kanabix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exobe.Utils.CommonFunctions
import com.exobe.util.DateFormat
import com.kanabix.R
import com.kanabix.api.response.CategoryManagementDoc
import com.kanabix.api.response.DealListDoc
import com.kanabix.interfaces.DealProductClick
import com.kanabix.models.DealsModelClass
import java.util.*
import kotlin.collections.ArrayList

class DealsAdapter(
    var context: Context, var data: ArrayList<DealListDoc>, var dealProductClick: DealProductClick
) :
    RecyclerView.Adapter<DealsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(context)
        val view: View = mInflater.inflate(R.layout.deals_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Data = data[position]

        Glide.with(context).load(Data.thumbnail).placeholder(R.drawable.placholder)
            .into(holder.item_image)
        holder.name.setText(Data.productId.productName)
        holder.cut_price.setText("${CommonFunctions.currencyFormatter(Data.productId.price.toDouble())}")
        holder.cut_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.price.setText("${CommonFunctions.currencyFormatter(Data.dealPrice.toDouble())}")
        holder.discount.text = "${Data.discount}% Off"

        holder.viewButton.setOnClickListener {
            dealProductClick.DealProductClick(Data.id)
        }

        holder.expiredText.visibility = View.GONE
        holder.webView.visibility = View.VISIBLE

        val countdowntimerdate = DateFormat.getDateOfhourminute2(Data.dealEndTime)
        val htmlData =
            "<!DOCTYPE HTML><html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>p{text-align: center;font-size: 15px;margin-top: 0px;color: rgb(191,30,46);}</style></head><body><p id=\"demo\"></p><script>var countDownDate = new Date(\"${countdowntimerdate}\").getTime();var x = setInterval(function() {var now = new Date().getTime();var distance = countDownDate - now;var days = Math.floor(distance / (1000 * 60 * 60 * 24));var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));var seconds = Math.floor((distance % (1000 * 60)) / 1000);document.getElementById(\"demo\").innerHTML = days + \"d \" + hours + \"h \"+ minutes + \"m \" + seconds + \"s \";if (distance < 0) {clearInterval(x);document.getElementById(\"demo\").innerHTML = \"EXPIRED\";}}, 1000);</script></body></html>"

        holder.webView.getSettings().javaScriptEnabled = true
        holder.webView.loadData(htmlData, "text/html", null)

    }


    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_image: ImageView
        var cut_price: TextView
        var expiredText: TextView
        var price: TextView
        var name: TextView
        var discount: TextView
        var viewButton: LinearLayout
        var webView: WebView

        init {
            item_image = itemView.findViewById(R.id.item_image)
            cut_price = itemView.findViewById(R.id.cut_price)
            expiredText = itemView.findViewById(R.id.expiredText)
//            lastdate = itemView.findViewById(R.id.lastdate)
            discount = itemView.findViewById(R.id.discount)
            price = itemView.findViewById(R.id.price)
            name = itemView.findViewById(R.id.name)
            viewButton = itemView.findViewById(R.id.viewButton)
            webView = itemView.findViewById(R.id.webview)
        }
    }
}




