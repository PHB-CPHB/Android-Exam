package exam.app.firebase

import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import exam.app.ActivityMain
import exam.app.R
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import exam.app.App
import exam.app.Entity.Friend
import exam.app.Entity.Message
import exam.app.Entity.Status
import exam.app.Entity.User
import exam.app.database.DBController
import exam.app.rest.APIService
import org.json.JSONObject


class MessagingService : FirebaseMessagingService(){

    val TAG : String = "MessagingService"
    override fun onMessageReceived(p0: RemoteMessage?) {

        if(p0!!.data.size > 0){
            Log.d(TAG, "Message data payload: " + p0!!.data)
            DBController.instance.insertMessage(Message(
                    friendEmail = p0!!.data["fromEmail"]!!,
                    friendPhone = p0!!.data["uPhone"]!!.toString(),
                    message = p0!!.data["uMsg"]!!,
                    status = Status.RECEIVED
            ))

            Log.d(TAG, p0!!.data["fromEmail"])
            Log.d(TAG, p0!!.data["uPhone"])
            Log.d(TAG, p0!!.data["uMsg"])

            val friend = DBController.instance.getFriendByPhone(p0!!.data["uPhone"].toString())

            if (friend.displayname.isNullOrEmpty()){
                APIService.matchFriend(p0!!.data["uPhone"])
            }

            val notificationBuilder : NotificationCompat.Builder =
                    NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle(p0!!.data.get("displayName"))
                            .setContentText(p0!!.data.get("uMsg"))
            var resultIntent : Intent  = Intent(this, ActivityMain::class.java)
            var resultPendingIntent = PendingIntent.getActivity(
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