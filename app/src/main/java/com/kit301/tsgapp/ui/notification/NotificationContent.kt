package com.kit301.tsgapp.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kit301.tsgapp.DrawerBaseActivity
import com.kit301.tsgapp.databinding.ActivityNotificationContentBinding
import com.kit301.tsgapp.ui.favourite.FavouriteItems

class NotificationContent : DrawerBaseActivity() {

    private lateinit var ui : ActivityNotificationContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNotificationContentBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //Retrieve the product extra information from previous page
        val notificationID =intent.getIntExtra(NotificationIndex, -1)
        val notificationObject = NotificationItems[notificationID]

        //Setup the user interface
        setupUI(notificationObject)


    }

    private fun setupUI(notificationObject: NotificationData) {
        ui.notificationContentTitle.text = notificationObject.title
        ui.notificationContentTime.text = notificationObject.time
        ui.notificationContentBody.text = notificationObject.message

    }


}