package com.kanabix.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.kanabix.R
import com.kanabix.customclicks.NotificationDeleteListener
import com.kanabix.customclicks.deleteCartItemListener

class NotificationDeleteDialog(
    var delete: NotificationDeleteListener,
    var id: String,
    var Title : String
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.notification_delete_layout, container, false)
        val no_btn = view.findViewById<Button>(R.id.no_button)
        val yes_btn = view.findViewById<Button>(R.id.yes_button)

        val title =view.findViewById<TextView>(R.id.title)

        title.text = Title

        no_btn.setOnClickListener {
            dismiss()
        }
        yes_btn.setOnClickListener {
            delete.notificationDeleteClick(id)
            dismiss()
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}