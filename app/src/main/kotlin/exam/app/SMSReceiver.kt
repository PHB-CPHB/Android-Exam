package exam.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast
import android.support.v4.app.NotificationCompat.getExtras
import android.support.v4.app.NotificationCompat.getExtras
import android.util.Log
import java.util.*


/**
 * Created by Mikkel on 30/05/2017.
 */
class SMSReceiver : BroadcastReceiver(){


    val TAG = "SMSReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received SMS")

        val bundle = intent!!.extras
        if (bundle != null) {
            // get sms objects
            val pdus = bundle.get("pdus") as Array<Any>

            // large message might be broken into many
            val messages = arrayOfNulls<SmsMessage>(pdus.size)
            val sb = StringBuilder()
            for (i in pdus.indices) {
                val format = bundle.getString("format")
                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                sb.append(messages[i]!!.getMessageBody())
            }
            val sender = messages[0]!!.originatingAddress.substring(3)
            val message = sb.toString()
            Log.d(TAG, message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // prevent any other broadcast receivers from receiving broadcast
            // abortBroadcast();

            App.instance.activityInstance!!.updateInbox(message, sender)
        }

    }




}