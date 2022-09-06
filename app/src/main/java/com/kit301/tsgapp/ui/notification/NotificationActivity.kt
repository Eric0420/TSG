package com.kit301.tsgapp.ui.notification

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityHomepageBinding
import com.kit301.tsgapp.databinding.ActivityNotificationBinding
import com.kit301.tsgapp.databinding.NotificationListItemBinding
import com.kit301.tsgapp.ui.test.FIREBASE_TAG

const val NotificationIndex = "Notification_Index"
val NotificationItems = mutableListOf<NotificationData>()

class NotificationActivity : DrawerBaseActivity() {

    private lateinit var ui : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(ui.root)
        setActionbarTitle()

        //link the RecyclerView to adapter
        ui.notificationList.adapter = NotificationAdapter(notification = NotificationItems)

        //Tell RecyclerView how to display the items
        //vertical list
        ui.notificationList.layoutManager = LinearLayoutManager(this)


        //Get database connection and connect to database
        val db = Firebase.firestore
        var notificationCollection = db.collection("Notification")
        .orderBy("time", Query.Direction.DESCENDING)

        notificationCollection
                .get()
                .addOnSuccessListener { result ->
                    NotificationItems.clear()  //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                    Log.d(FIREBASE_TAG, "--- all Notification ---")
                    for (document in result)
                    {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val notificationMessages = document.toObject<NotificationData>()
                        notificationMessages.id = document.id
                        Log.d(FIREBASE_TAG, notificationMessages.toString())

                        NotificationItems.add(notificationMessages)
                    }
                    (ui.notificationList.adapter as NotificationAdapter).notifyDataSetChanged()
                }

    }

    private fun setActionbarTitle() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val currentLanguageSetting = sharedPreferences.getString("My_Lang", "")
        if (currentLanguageSetting == "en" || currentLanguageSetting == ""){
            allocateActivityTitle("Notification")
        }
        else if (currentLanguageSetting == "zh"){
            allocateActivityTitle("通知")
        }
    }

    //Bind the list (notification_list_item.xml) with the RecyclerView
    inner class NotificationHolder(var ui: NotificationListItemBinding) : RecyclerView.ViewHolder(ui.root) {}

    //This is the controller that handles the communication between our model and our view
    inner class NotificationAdapter(private val notification: MutableList<NotificationData>) : RecyclerView.Adapter<NotificationHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
            val ui = NotificationListItemBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return NotificationHolder(ui)                                             //wrap it in a ViewHolder
        }

        override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
            val notificationItem = notification[position]   //get the data at the requested position
            holder.ui.notificationTitle.text = notificationItem.title //set the TextView in the row we are recycling
            holder.ui.notificationTime.text = notificationItem.time

            holder.itemView.setOnClickListener {
                var intent = Intent(holder.itemView.context, NotificationContent::class.java)
                intent.putExtra(NotificationIndex, position)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return notification.size
        }
    }


}