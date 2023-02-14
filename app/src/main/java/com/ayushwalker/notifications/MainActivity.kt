package com.ayushwalker.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btnShowNotification:Button

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName" // It doesn't really matter, what id and name you are giving
    val NOTIFICATION_ID = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnShowNotification = findViewById(R.id.btnShowNotification)

        createNotificationChannel() // We only do these only one time, when the app starts, not before we show any notification

        // ------------- CREATING PENDING INTENT PART -------------------------
        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent) // this will just this activity that will open on that notification Click
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // ------------ PENDING INTENT PART COMPLETED -----------------------

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Awesome Notification")
            .setContentText("This is the Content Text")
            .setSmallIcon(R.drawable.ic_star)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // This helps in notification Click to open the app again when m
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        btnShowNotification.setOnClickListener {
            // this again will take an notificationId
            notificationManager.notify(NOTIFICATION_ID,notification) // @RequiresPermission("android.permission.POST_NOTIFICATIONS")
        }

        // Above this line , we just create the notification, but on clicking on the notification, nothing happens,
        // generally, we like that when we click no the notification, desired work happen (like, opening of the app, etc..) to do that..!




    }

    fun createNotificationChannel(){
        // Since we don't need to create notification channel before android Oreo, We need to Check whether the system is under Android Oreo or after.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}
/*
NOTES/STEPS:
1. Before android Oreo, it was quite Easier to create a notification, but since then we need what we called a notification channel
2. The First thing need to do to create a notification Channel is to define notificationID and notificationName, we do it globally..
3. After that we need to create a function, for ex: createNotificationChannel,
4. After checking we pass our channelID, channelName, and set the important with the help of NotificationManager class and with the help of apply{}
    we can change light colors and enable lights and all.
5. After that we need to create a notification manager, (here we use getSystemService)
    NOTES: Since getSystemService extends gives Any Object as results, and in kotlin, everything extends from Any Object, and since we want to use
    notification service as Notification Manager as we want to use the return type as Notification Manager.
6. Create notification channel and we are good to go..!
7. Next step is to post our notification channel.
8. We do this by building a Notification.Builder class, and attach all the important methods to that..!
    val notification = Notification.Builder(this, CHANNEL_ID)

9. We make notificationManger using NotificationManagerCompat.from(this) and set the buttonClickListener
10. Under the button clickListener, we just notify the manager, it takes two argument, one is notificationId and other is the notification itself.
NOTES: Above this line , we just create the notification, but on clicking on the notification, nothing happens,
     generally, we like that when we click no the notification, desired work happen (like, opening of the app, etc..),
     So, when we get out of the app, the notification stills stays in the notification Bar, this is done by other application in the android
     named as Notification Manager , so we use pending Intent to solve that problem.
     Now to use PendingIntent, we need to first create Normal Intent..
     After completion of pending Intentpart , don't forget to add that on Notification.BUILDER class


REPORT: On Adding the Pending Intent, Don't know why, but my app crashed.. Will work on that also..!!
 */