package com.kanabix.utils

import android.content.Context
import android.database.Cursor
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Current_location (var context: Context) {

///////////////////////////////// LatLong TO getAddress /////////////////////////////////

    fun getAddress(lat: Double, lng: Double): ArrayList<String> {
        val geocoder = Geocoder(context, Locale.getDefault())
        val AddressList = ArrayList<String>()

        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses!![0]
            var add = obj.getAddressLine(0)
            var countryName = (obj.countryName)
            var countryCode = (obj.countryCode)
            var adminArea = (obj.adminArea)
            var postalCode = (obj.postalCode)
            var subAdminArea = (obj.subAdminArea)
            var locality = (obj.locality)
//            var subThoroughfare = (obj.subThoroughfare)

            AddressList.add(countryName)
            AddressList.add(countryCode)
            AddressList.add(adminArea)
            AddressList.add(postalCode)
            AddressList.add(subAdminArea)
            AddressList.add(locality)
//            AddressList.add(subThoroughfare)

//            main(countrycode)
//               countrycodeText.text = getCurrencySymbol("USD")
//            Toast.makeText(this, countrycode, Toast.LENGTH_SHORT).show()

//            GUIStatics.currentAddress = (obj.subAdminArea + "," + obj.adminArea)
//            GUIStatics.latitude = obj.latitude
//            GUIStatics.longitude = obj.longitude
//            GUIStatics.currentCity = obj.subAdminArea
//            GUIStatics.currentState = obj.adminArea
            add = """
            $add
            ${obj.countryName}
            """.trimIndent()
            add = """
            $add
            ${obj.countryCode}
            """.trimIndent()
            add = """
            $add
            ${obj.adminArea}
            """.trimIndent()
            add = """
            $add
            ${obj.postalCode} """.trimIndent()
            add = """
            $add
            ${obj.subAdminArea}
            """.trimIndent()
            add = """
            $add
            ${obj.locality}
            """.trimIndent()
            add = """
            $add
            ${obj.subThoroughfare}
            """.trimIndent()
            Log.v("IGA", "Address$add")
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
        }

        return (AddressList)

    }

    fun getonlyAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val AddressList = ArrayList<String>()
        var add=""
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses!![0]
            add = obj.getAddressLine(0)
            var countryName = (obj.countryName)
            var countryCode = (obj.countryCode)
            var adminArea = (obj.adminArea)
            var postalCode = (obj.postalCode)
            var subAdminArea = (obj.subAdminArea)
            var locality = (obj.locality)

            AddressList.add(countryName)
            AddressList.add(countryCode)
            AddressList.add(adminArea)
            AddressList.add(postalCode)
            AddressList.add(subAdminArea)
            AddressList.add(locality)

        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()

        }

        return (add)

    }
    ///////////////////////////////// Countrycode TO (locale + currency + symbol) /////////////////////////////////
    fun main(countrycode: String): ArrayList<String> {
        val map: Map<Currency, Locale> = getCurrencyLocaleMap()
        val countries = arrayOf(countrycode)
        val countryCredential = ArrayList<String>()

        for (countryCode in countries) {
            val locale = Locale("EN", countryCode)
            val currency = Currency.getInstance(locale)
            val symbol = currency.getSymbol(map[currency])
            countryCredential.add(locale.toString())
            countryCredential.add(currency.toString())
            countryCredential.add(symbol.toString())

        }
        return (countryCredential)
    }

    fun getCurrencyLocaleMap(): Map<Currency, Locale> {
        val map: MutableMap<Currency, Locale> = HashMap()
        for (locale in Locale.getAvailableLocales()) {
            try {
                val currency = Currency.getInstance(locale)
                map[currency] = locale
            } catch (e: java.lang.Exception) {
                // skip strange locale
            }
        }
        return map
    }

    fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )


        return image
    }
    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

}
