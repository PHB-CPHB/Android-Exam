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
import exam.app.ActivityMain
import kotlinx.android.synthetic.main.fragment_am_new_message.view.*
import org.jetbrains.anko.onClick
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus


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
        //val fragmentChat = inflater.inflate(R.layout.fragment_am_chat, container, false)
        getPermissionToReadSMS()
        fragment.sendMSg.onClick {
            onSendClick(inputMSG.text.toString(), reciever.text.toString())
            (activity as ActivityMain).showChat()
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

            } else {
                Toast.makeText(App.instance, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendClick(inputString : String, recieverString : String) {
        var input: String = inputString
        var reciever: String = recieverString
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS()
        } else {
            if (input == null && reciever == null || input == null && reciever == "" || input == "" && reciever == null) {
                return Toast.makeText(App.instance, "OOppps something went wrong", Toast.LENGTH_SHORT).show()
            } else {
                smsManager.sendTextMessage(reciever, null, input, null, null)
                Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()

            }
        }
    }

}