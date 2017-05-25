package exam.app.layout

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.R
import android.widget.Toast
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import android.support.v4.content.ContextCompat
import exam.app.Manifest
import exam.app.App
import android.text.method.TextKeyListener.clear
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_am_new_message.*
import android.Manifest.permission.SEND_SMS
import android.telephony.SmsManager
import android.widget.EditText
import android.util.Log
import kotlinx.android.synthetic.main.fragment_am_new_message.view.*
import org.jetbrains.anko.onClick


class AMNewMessage : Fragment() {
    val TAG = "AMNewMessage"
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {
        /**
         *  Inflate the layout for this fragment
         *  Takes the XML code and makes the view.
         *  ! All buttons should be with fragment
         *  EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_new_message, container, false)
        getPermissionToReadSMS()
        fragment.sendMSg.onClick {
            onSendClick()
        }
        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */

        return fragment
    }

    //val arrayAdapter : ArrayAdapter()
    private val READ_SMS_PERMISSIONS_REQUEST = 1

    fun getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_SMS)) {
                Toast.makeText(App.instance, "Please allow permission!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf<String>(android.Manifest.permission.READ_SMS),
                    READ_SMS_PERMISSIONS_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(App.instance, "Read SMS permission granted", Toast.LENGTH_SHORT).show()
                refreshSmsInbox()
            } else {
                Toast.makeText(App.instance, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun refreshSmsInbox() {
        val contentResolver : ContentResolver = activity.contentResolver

        val smsInboxCursor : Cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        if (smsInboxCursor == null) return
        val indexBody = smsInboxCursor.getColumnIndex("body")
        val indexAddress = smsInboxCursor.getColumnIndex("address")
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return
        //arrayAdapter.clear()
        do {
            val str = """
                |SMS From: ${smsInboxCursor.getString(indexAddress)}
                |${smsInboxCursor.getString(indexBody)}
                |
                """.trimMargin()
            //arrayAdapter.add(str)
            messages.text = str

        } while (smsInboxCursor.moveToNext())
    }


    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendClick() {
        var input: String = inputMSG!!.text.toString()
        var reciever: String = reciever!!.text.toString()
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS()
        } else {
            smsManager.sendTextMessage(reciever, null, input, null, null)
            Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()
           
        }
    }


}