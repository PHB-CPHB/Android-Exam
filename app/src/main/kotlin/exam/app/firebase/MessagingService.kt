package exam.app.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import exam.app.App

/**
 * Created by Mikkel on 14/05/2017.
 */
class MessagingService : FirebaseMessagingService(){
    val TAG : String = "MessagingService"
    override fun onMessageReceived(p0: RemoteMessage?) {
        Log.d(TAG, "Message data payload: " + p0!!.data)
        App.instance
    }
}