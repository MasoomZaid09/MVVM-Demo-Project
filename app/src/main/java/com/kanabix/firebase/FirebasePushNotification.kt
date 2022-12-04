package com.kanabix.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kanabix.R
import com.kanabix.api.response.response
import com.kanabix.ui.acitivity.FragmentContainerActivity
import com.kanabix.utils.SavedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebasePushNotification: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {

            if (remoteMessage.data.size > 0){
                getRemoteView(remoteMessage.data)
            }else{

            }
        }
    }

    private fun getRemoteView(data: Map<String, String>) {

        val intent = Intent(this, FragmentContainerActivity::class.java)
        intent.putExtra("fcm","fcm")
        intent.putExtra("token",SavedPrefManager.getStringPreferences(this,SavedPrefManager.TOKEN))
        var pendingIntent: PendingIntent? = null
        val CHANNEL_ID = "my_channel_01"

        try {
            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }catch (e:Exception){
            e.printStackTrace()
        }



        val notificationId: Int = SavedPrefManager.getIntPreferences(this, SavedPrefManager.NOTIFICATION_ID)

        val notification: Notification
        var bitmap: Bitmap?=null
        if(data["thumbnails"]==null||data["thumbnails"].equals(""))
        {
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load(R.drawable.half_splash)
                .placeholder(R.drawable.half_splash)
                .submit()
            try {
                bitmap = futureTarget.get()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        else
        {
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load(data!!["thumbnails"])
                .placeholder(R.drawable.half_splash)
                .submit()
            try {
                bitmap = futureTarget.get()
            }catch (e:Exception){
                e.printStackTrace()
            }

        }


        if (Build.VERSION.SDK_INT >= 26) {
            //This only needs to be run on Devices on Android O and above
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val id = "YOUR_CHANNEL_ID"
            val name: CharSequence = "YOUR_CHANNEL NAME" //user visible
            val description = "YOUR_CHANNEL_DESCRIPTION" //user visible
            val importance = NotificationManager.IMPORTANCE_MAX
            @SuppressLint("WrongConstant") val mChannel = NotificationChannel(id, name, importance)
            mChannel.description = description
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            mChannel.canShowBadge()
            mChannel.setShowBadge(true)
            mChannel.vibrationPattern = longArrayOf(0, 1000)
            mNotificationManager.createNotificationChannel(mChannel)

            notification = Notification.Builder(applicationContext, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.half_splash)
                .setContentTitle( data["subject"])
                .setContentText(data["body"])
                .setTicker(getString(R.string.app_name))
                .setLargeIcon(bitmap)
                .setAutoCancel(true) // .setLargeIcon(Bitmap.createScaledBitmap(notifyImage, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(false).build()

            Log.e("Hello",data["body"].toString())

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager != null) {
                notificationManager.notify(notificationId, notification)
                SavedPrefManager.saveIntPreferences(this, SavedPrefManager.NOTIFICATION_ID,notificationId + 1)
            }
        }
        else {
            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.half_splash)
                .setContentTitle(getString(R.string.app_name))
                .setLargeIcon(bitmap)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setContentTitle( data!!["subject"])
                .setContentText(data!!["body"])
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
            notificationBuilder.setContentIntent(pendingIntent)

            Log.e("Hello",data["body"].toString())

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager != null) {
                notificationManager.notify(notificationId, notificationBuilder.build())
                SavedPrefManager.saveIntPreferences(this, SavedPrefManager.NOTIFICATION_ID,notificationId + 1

                )
            }
        }
    }


    override fun onNewToken(token: String) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            SavedPrefManager.saveStringPreferences(
                applicationContext,
                SavedPrefManager.KEY_DEVICE_TOKEN,
                token
            )

        })
    }

}