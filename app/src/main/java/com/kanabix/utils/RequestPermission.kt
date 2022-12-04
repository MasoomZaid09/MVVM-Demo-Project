package  com.example.complyany.entity.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.kanabix.utils.LocationClass
import com.kanabix.utils.ProgressBar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RequestPermission {

    companion object{
        fun requestMultiplePermissions(context: Context){
            Dexter.withActivity(context as Activity?)
                .withPermissions(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,

                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted


                        if (report.areAllPermissionsGranted()){

                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    context,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    1000
                                )
                            } else {
                                LocationClass.getCurrentLocation(context)
                            }
                        }

                        if (report.deniedPermissionResponses.size != 0){

                            for (i in 0 until report.deniedPermissionResponses.size){
                                if (report.deniedPermissionResponses.get(i).permissionName.equals("android.permission.ACCESS_FINE_LOCATION")){
                                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        if (report.grantedPermissionResponses.size != 0){

                            for (i in 0 until report.grantedPermissionResponses.size){
                                if (report.grantedPermissionResponses.get(i).permissionName.equals("android.permission.ACCESS_FINE_LOCATION")){
                                    if (ActivityCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        ActivityCompat.requestPermissions(
                                            context,
                                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                            1000
                                        )
                                    } else {
                                        LocationClass.getCurrentLocation(context)
                                    }
                                }
                            }
                        }
                    }



                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                }).withErrorListener {

                }
                .onSameThread()
                .check()
        }

    }

}