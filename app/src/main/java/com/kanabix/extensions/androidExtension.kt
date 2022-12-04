package com.kanabix.extensions

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.*
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import com.kanabix.R
import com.kanabix.interfaces.LogOutListener
import com.kanabix.interfaces.PermissionDeniedListener
import com.kanabix.interfaces.RateAppClick
import com.kanabix.interfaces.paymentDialogClickListener


object androidExtension {

    fun showPaymentDialog(
        title: String,
        activity: Activity,
        click : paymentDialogClickListener,
    ) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.payment_successful_popup)
        dialog.setCancelable(false)
        val yesBtn = dialog.findViewById<Button>(R.id.btnPaymentContinue)
        val bodyTitle = dialog.findViewById<TextView>(R.id.totalPayment)
        bodyTitle.text = title
        yesBtn.setOnClickListener {

            click.paymentDialogClick()

            dialog.dismiss()
////            val fm:FragmentManager  = fragmentManager!!
////            for(i in 0 until fm.backStackEntryCount){
////                fm.popBackStack()
////            }
//
//            navController.navigate(R.id.action_paymentDetailsFragment_to_myOrdersFragment)
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }

    fun registerationPopUp(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.registeration_popup)
        val SendBtn = dialog.findViewById<Button>(R.id.btnSet)

        SendBtn.setOnClickListener {
            (context as Activity).finish()
            dialog.dismiss()
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun logOutDialog(activity: Activity, roleFlag: String,click : LogOutListener) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.log_out_design)
        val yesBtn = dialog.findViewById<Button>(R.id.btnYes)
        yesBtn.setOnClickListener {

            dialog.dismiss()
            click.logOutListener(roleFlag)

        }
        val noBtn = dialog.findViewById<Button>(R.id.btnNo)
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun rateThisApp(context: Context, click: RateAppClick) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.rate_app_design)
        val cancel = dialog.findViewById<ImageView>(R.id.imgCross)
        val rateThisApp = dialog.findViewById<RatingBar>(R.id.rateThisApp)
        val btnContinue = dialog.findViewById<Button>(R.id.btnRateContinue)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        btnContinue.setOnClickListener {
            if (rateThisApp.rating > 0.0) {
                click.rateThisApp(rateThisApp.rating.toInt())
                dialog.dismiss()
            }
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun alertBox(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun alertBoxWithFinish(message: String, activity: Activity) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setPositiveButton("ok") { dialogInterface, which ->
            activity.finish()
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun updateBox(message: String, context: Context, navController: NavController) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
            navController.popBackStack()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun filePermission(
        message: String,
        context: Context,
        permissionDeniedListener: PermissionDeniedListener
    ) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
            permissionDeniedListener.permissionDeniedListener()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun LottieAnimationView.initLoader(isLoading: Boolean) {
        if (isLoading) {
            playAnimation()
            visibility = View.VISIBLE
        } else {
            pauseAnimation()
            animation?.reset()
            visibility = View.GONE
        }
    }
}