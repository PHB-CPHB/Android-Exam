package exam.app.firebase

import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import exam.app.ActivityMain
import exam.app.R
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import exam.app.App
import exam.app.Entity.Message
import exam.app.Entity.Status
import exam.app.database.DBController
import exam.app.rest.APIService


class MessagingService : FirebaseMessagingService(){

    val TAG : String = "MessagingService"
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if(remoteMessage!!.data.isNotEmpty()){
            DBController.instance.insertMessage(Message(
                    friendEmail = remoteMessage.data["fromEmail"]!!,
                    friendPhone = remoteMessage.data["uPhone"]!!.toString(),
                    message = remoteMessage.data["uMsg"]!!,
                    status = Status.RECEIVED
            ))

            val friend = DBController.instance.getFriendByPhone(remoteMessage.data["uPhone"].toString())

            if (friend.displayname.isNullOrEmpty()){
                APIService.matchFriend(remoteMessage.data["uPhone"])
            }

            val notificationBuilder : NotificationCompat.Builder =
                    NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle("New message")
                            .setContentText(remoteMessage.data["uMsg"])

            val resultIntent : Intent  = Intent(this, ActivityMain::class.java)
            val resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            notificationBuilder.setVibrate(longArrayOf( 10, 400, 200, 400 ))

            notificationBuilder.setLights(Color.BLUE, 2000, 2000)
            notificationBuilder.setContentIntent(resultPendingIntent)
            notificationBuilder.setAutoCancel(true)
            val mNotificationId = 1
            val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(mNotificationId, notificationBuilder.build())

            App.instance.activityInstance!!.refreshChat(friend)

        }
    }
}