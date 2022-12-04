package com.kanabix.extensions

import android.R
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*


class DialogUtils {

    private val dialogList: MutableList<Dialog> = ArrayList()
    private val progressDialog: ProgressDialog? = null

    fun createDialog(context: Context?, dialogLayout: View?, animationType: Int): Dialog? {
        val dialog = Dialog(context!!, R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)
        dialog.setContentView(dialogLayout!!)
        val layoutParams: WindowManager.LayoutParams = dialog.getWindow()!!.getAttributes()
        layoutParams.dimAmount = 0.7f
        dialog.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.getWindow()?.setGravity(Gravity.CENTER)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.getWindow()?.setAttributes(layoutParams)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (animationType == 0) {
            dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.Animation_Dialog
        } else {
//            dialog.getWindow()?.getAttributes()?.windowAnimations = R.style.AnimationBottomPopUp
        }
        dialog.show()
        dialogList.add(dialog)
        return dialog
    }

}