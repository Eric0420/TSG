package com.kit301.tsgapp.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivitySendNotificationBinding
import com.kit301.tsgapp.ui.notification.NotificationData
import com.kit301.tsgapp.ui.notification.PushNotification
import com.kit301.tsgapp.ui.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class SendNotification : DrawerBaseActivity() {
    private lateinit var ui : ActivitySendNotificationBinding

    val TAG = "SendNotification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySendNotificationBinding.inflate(layoutInflater)
        setContentView(ui.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        ui.btnSend.setOnClickListener{
            val title = ui.etTitle.text.toString()
            val message = ui.etMessage.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }

    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")

            } else{
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }
}