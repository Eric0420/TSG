package com.kit301.tsgapp.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.FIREBASE_TAG
import com.kit301.tsgapp.Product
import com.kit301.tsgapp.databinding.ActivitySendNotificationBinding
import com.kit301.tsgapp.ui.notification.NotificationData
import com.kit301.tsgapp.ui.notification.PushNotification
import com.kit301.tsgapp.ui.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val TOPIC = "/topics/myTopic"

class SendNotification : DrawerBaseActivity() {
    private lateinit var ui : ActivitySendNotificationBinding

    val TAG = "SendNotification"
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivitySendNotificationBinding.inflate(layoutInflater)
        setContentView(ui.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        ui.btnSend.setOnClickListener{
            val title = ui.etTitle.text.toString()
            val message = ui.etMessage.text.toString()

            //Get the current date and time
            val datetimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date = Date()
            val time = datetimeFormat.format(date)

            if (title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message,id = null, time),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
                storeNotification(title, message, time)
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

    private fun storeNotification(title: String, message: String, time: String){

        val notificationCollection = db.collection("Notification")

        val notification = NotificationData(
                title = title,
                message = message,
                time = time
        )


        //Insert the notification into the Firebase database
        notificationCollection.document(notification.time!!)
                .set(notification)
                .addOnSuccessListener {
                    Log.d(FIREBASE_TAG, "Notification created with title")
                }
                .addOnFailureListener{
                    Log.e(FIREBASE_TAG, "Error writing document")
                }

    }


}


