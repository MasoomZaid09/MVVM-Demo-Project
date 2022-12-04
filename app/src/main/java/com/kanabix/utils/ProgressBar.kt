package com.kanabix.utils

import android.app.ProgressDialog
import android.content.Context
import android.view.WindowManager
import com.kanabix.R

object ProgressBar {

    var dialog: ProgressDialog? = null

    fun showProgress(context: Context?) {
        dialog = ProgressDialog(context)
        try {
            dialog!!.show()
        } catch (e: WindowManager.BadTokenException) {
        }
//        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.loader)
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    fun hideProgress() {
        if (dialog != null) dialog!!.dismiss()
    }
}