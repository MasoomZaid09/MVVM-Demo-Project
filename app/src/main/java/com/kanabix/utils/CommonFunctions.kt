package com.exobe.Utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CommonFunctions {

    fun currencyFormatter(amount: Double): String {
        var finalValue = ""
        try {
            val COUNTRY = "in"
            val LANGUAGE = "en"
            finalValue =
                NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(amount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalValue
    }

}