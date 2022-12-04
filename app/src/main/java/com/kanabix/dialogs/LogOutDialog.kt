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
import com.kanabix.interfaces.sessionExpiredListener

class LogOutDialog(
    var click : sessionExpiredListener
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.session_expire, container, false)
        val yes_btn = view.findViewById<Button>(R.id.Ok_Button)

        yes_btn.setOnClickListener {
            click.sessionExpiredClick()
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